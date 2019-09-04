package io.lightflame.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;


/**
 * HTTPSession
 */
public class HTTPSession {

    private final SocketChannel channel;
    private Charset charset = Charset.forName("UTF-8");
    private CharsetEncoder encoder = charset.newEncoder();
    private final ByteBuffer buffer = ByteBuffer.allocate(2048);
    private final StringBuilder rl = new StringBuilder();
    private HTTPRequest request;
    private int mark = 0;

    @Override
    public String toString() {
        try {
            return String.format("%s - %s", channel.getRemoteAddress().toString(), rl.toString());
        }catch(Exception e){
            return "";
        }
    }

    public HTTPSession(SocketChannel channel) {
        this.channel = channel;
    }

    public HTTPSession() {
        channel = null;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(HTTPRequest request) {
        this.request = request;
    }

    /**
     * @return the request
     */
    public HTTPRequest getRequest() {
        return request;
    }


    StringBuilder readLines(){
        return rl;
    }

    public byte[] writeBody(){
        byte[] arr = new byte[buffer.remaining()];
        while (buffer.hasRemaining()) {
            buffer.get(arr);
        }
        return arr;
    }
    /**
     * Try to read a line.
     */
    String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int l = -1;
        while (buffer.hasRemaining()) {
            char c = (char) buffer.get();
            sb.append(c);
            if (c == '\n' && l == '\r') {
                // mark our position
                mark = buffer.position();
                // append to the total
                rl.append(sb);
                // return with no line separators
                return sb.substring(0, sb.length() - 2);
            }
            l = c;
        }
        return null;
    }


    /**
     * Get more data from the stream.
     */
    void readData() throws IOException {
        buffer.limit(buffer.capacity());
        int read = channel.read(buffer);
        if (read == -1) {
            return;
            // throw new IOException("End of stream");
        }
        buffer.flip();
        buffer.position(mark);
    }

    private void writeLine(String line) throws IOException {
        channel.write(encoder.encode(CharBuffer.wrap(line + "\r\n")));
    }

    void sendResponse(HTTPResponse response) {
        response.addDefaultHeaders();
        try {
            writeLine(response.getVersion() + " " + response.getResponseCode() + " " + response.getResponseReason());
            for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
                writeLine(header.getKey() + ": " + header.getValue());
            }
            writeLine("");
            channel.write(ByteBuffer.wrap(response.getContent()));
        } catch (IOException ex) {
            // slow silently
        }
    }

    void close() {
        try {
            channel.close();
        } catch (IOException ex) {
        }
    }

}