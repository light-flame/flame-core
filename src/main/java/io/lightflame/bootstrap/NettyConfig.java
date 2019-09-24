package io.lightflame.bootstrap;


import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.lightflame.nsqconsumer.NsqConsumerHandler;
import io.lightflame.tcp.TcpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * NettyConfig
 */
public class NettyConfig {

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static ChannelFuture futureCh;
    private static Map<Integer, Listener> portMap = new HashMap<>();

    public enum Listener{
        HTTP_WS,TCP_SERVER,NSQ_CONSUMER
    }

    static public Integer getAvaliablePort(Listener value){
        
        for (Entry<Integer, Listener> ks : portMap.entrySet()){
            if (ks.getValue().equals(value)){
                return ks.getKey();
            }
        }
        return null;
    }
    

    static void newHttpWsListener(int port){
        if (portMap.containsKey(port)){
            return;
        }
        try {
            ServerBootstrap http = new ServerBootstrap();
            http.option(ChannelOption.SO_BACKLOG, 1024);
            http.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new PipelineFactory(null));
    
            futureCh = http.bind(port).sync();
            portMap.put(port, Listener.HTTP_WS);
        } catch (Exception e) {
            
        }
    }

    static void newServerTcpChannel(String host, int port){
        if (portMap.containsKey(port)){
            return;
        }
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress(new InetSocketAddress(host, port));
        
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new TcpHandler());
                }
            });
            futureCh = serverBootstrap.bind().sync();
            portMap.put(port, Listener.TCP_SERVER);
        }catch (Exception e){
        }
    }

    static void newNsqConsumer(String host, int port){
        if (portMap.containsKey(port)){
            return;
        }
        try {
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(bossGroup);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress(host, port));
            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new NsqConsumerHandler());
                }
            });
            futureCh = clientBootstrap.connect().sync();
            portMap.put(port, Listener.NSQ_CONSUMER);
        }catch (Exception e){
            System.err.println(e);
        }
    }

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

    static void start() {
        
        SslContext sslCtx;
        try {
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            try {
                if (futureCh != null){
                    futureCh.channel().closeFuture().sync();
                }
                // ch2.closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
            }catch(Exception e){}
       



    }
}