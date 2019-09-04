package io.lightflame.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.apache.log4j.Logger;



/**
 * WebServer
 */
public class WebServer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(WebServer.class);

    private Selector selector = Selector.open();
    private ServerSocketChannel server = ServerSocketChannel.open();
    private HTTPHandlers httpHandlers = new HTTPHandlers();
    private boolean isRunning = true;
    private boolean debug = true;

    /**
     * Create a new server and immediately binds it.
     *
     * @param address the address to bind on
     * @throws IOException if there are any errors creating the server.
     */
    public WebServer(InetSocketAddress address) throws Exception {
        server.socket().bind(address);
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
       
    }

    /**
     * Handle a web request.
     *
     * @param session the entire http session
     * @return the handled request
     */
    protected HTTPResponse handle(HTTPSession session) {
        return httpHandlers.getHandle(session);
    }


    /**
     * Shutdown this server, preventing it from handling any more requests.
     */
    public final void shutdown() {
        isRunning = false;
        try {
            selector.close();
            server.close();
        } catch (IOException ex) {
            // do nothing, its game over
        }
    }

    public final void run() {
        if (isRunning) {
            try {
                selector.selectNow();
                Iterator<SelectionKey> i = selector.selectedKeys().iterator();
                while (i.hasNext()) {
                    SelectionKey key = i.next();
                    i.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    try {
                        // get a new connection
                        if (key.isAcceptable()) {
                            // accept them
                            SocketChannel client = server.accept();
                            // non blocking please
                            client.configureBlocking(false);
                            // show out intentions
                            client.register(selector, SelectionKey.OP_READ);
                            // read from the connection
                        } else if (key.isReadable()) {
                            // get the client
                            SocketChannel client = (SocketChannel) key.channel();
                            // get the session
                            HTTPSession session = (HTTPSession) key.attachment();
                            // create it if it doesnt exist
                            if (session == null) {
                                session = new HTTPSession(client);
                                key.attach(session);
                            }
                            // get more data
                            session.readData();
                            // decode the message
                            String line;
                            while ((line = session.readLine()) != null) {
                                // check if we have got everything
                                if (line.isEmpty()) {
                                    session.setRequest(new HTTPRequest(session.readLines().toString()));
                                    session.sendResponse(handle(session));
                                    session.close();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        LOGGER.error("Error handling client: " + key.channel());
                        if (debug) {
                            ex.printStackTrace();
                        } else {
                            LOGGER.error(ex);
                            LOGGER.error("\tat " + ex.getStackTrace()[0]);
                        }
                        if (key.attachment() instanceof HTTPSession) {
                            ((HTTPSession) key.attachment()).close();
                        }
                    }
                }
            } catch (IOException ex) {
                // call it quits
                shutdown();
                // throw it as a runtime exception so that Bukkit can handle it
                throw new RuntimeException(ex);
            }
        }
    }

}