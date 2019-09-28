package io.lightflame.bootstrap;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.lightflame.nsqconsumer.NsqConfig;
import io.lightflame.nsqconsumer.NsqConsumerHandler;
import io.lightflame.tcp.TcpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
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

/**
 * NettyConfig
 */
public class NettyConfig {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Map<Integer, Listener> listeners = new HashMap<>();


    interface Listener{
        void bind() throws InterruptedException;
        void sync()throws InterruptedException;
        Integer port();
        void close();
    }

    static class HttpWsServerListener implements Listener{
        private ServerBootstrap serverBs;
        private Integer port;
        private Channel ch;

        HttpWsServerListener(ServerBootstrap sb, Integer p){
            this.serverBs = sb;
            this.port = p;
        }

        @Override
        public void bind() throws InterruptedException {
            this.ch = serverBs.bind(this.port).sync().channel();
        }

        @Override
        public void sync()throws InterruptedException {
            this.ch.closeFuture().sync();
        }

        @Override
        public Integer port() {
            return this.port;
        }

        @Override
        public void close() {
            this.ch.close();
        }
    }
    static class TcpServerListener implements Listener{
        private ServerBootstrap serverBs;
        private Integer port;
        private Channel ch;

        TcpServerListener(ServerBootstrap sb, Integer p){
            this.serverBs = sb;
            this.port = p;
        }

        @Override
        public void bind() throws InterruptedException {
            this.ch = serverBs.bind(this.port).sync().channel();
        }

        @Override
        public void sync()throws InterruptedException {
            this.ch.closeFuture().sync();
        }

        @Override
        public Integer port() {
            return this.port;
        }

        @Override
        public void close() {

        }
    }

    static class NsqConsumerListener implements Listener{
        private Bootstrap cb;
        private Integer port;
        private ChannelFuture ch;

        NsqConsumerListener(Bootstrap cb, Integer p){
            this.cb = cb;
            this.port = p;
        }

        @Override
        public void bind() throws InterruptedException {
            this.ch = cb.connect().sync();
        }

        @Override
        public void sync()throws InterruptedException {
            this.ch.channel().closeFuture().sync();
        }

        @Override
        public Integer port() {
            return this.port;
        }

        @Override
        public void close() {

        }

    }

    

    void newHttpWsListener(int port){
        ServerBootstrap http = new ServerBootstrap();
        http.option(ChannelOption.SO_BACKLOG, 1024);
        http.group(bossGroup, workerGroup);
        http.channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new PipelineFactory(port, null));
        Listener listener = new HttpWsServerListener(http, port);
        bindAndPut(listener, port);
    }

    void newServerTcpChannel(String host, int port){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(new InetSocketAddress(host, port));
    
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new TcpHandler());
            }
        });
        Listener listener = new TcpServerListener(serverBootstrap, port);
        bindAndPut(listener, port);
    }

    void newNsqConsumer(NsqConfig config){
        Bootstrap clientBootstrap = new Bootstrap();

        clientBootstrap.group(bossGroup);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.remoteAddress(config.socketAddress());
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NsqConsumerHandler(config));
            }
        });
        Listener listener =  new NsqConsumerListener(clientBootstrap, config.port());
        bindAndPut(listener, config.port());
    }

    void bindAndPut(Listener listener, int port){
        try {
            listener.bind();
            listeners.put(port, listener);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    void closeListenerByPort(int port){
        listeners.remove(port).close();
    }

    final boolean SSL = System.getProperty("ssl") != null;
    final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

    void start() {
        
        try {
            for (Listener listener : listeners.values()){
                listener.sync();
            }
        }catch(Exception e){
            throw  new RuntimeException(e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}