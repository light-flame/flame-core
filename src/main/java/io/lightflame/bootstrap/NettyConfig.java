package io.lightflame.bootstrap;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

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

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();
    private static List<Listener> listeners = new ArrayList<>();

    static public Integer getAvaliablePort(ListenerKind lk){
        for (Listener listener : listeners){
            if (listener.kind().equals(lk)){
                return listener.port();
            }
        }
        return null;
    }

    public enum ListenerKind{
        HTTP_WS,TCP_SERVER,NSQ_CONSUMER
    }


    interface Listener{
        void bind() throws InterruptedException;
        void sync()throws InterruptedException;
        Integer port();
        ListenerKind kind();
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
        public ListenerKind kind() {
            return ListenerKind.HTTP_WS;
        }

        @Override
        public Integer port() {
            return this.port;
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
        public ListenerKind kind() {
            return ListenerKind.TCP_SERVER;
        }

        @Override
        public Integer port() {
            return this.port;
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
        public ListenerKind kind() {
            return ListenerKind.NSQ_CONSUMER;
        }

        @Override
        public Integer port() {
            return this.port;
        }
        
    }

    

    static void newHttpWsListener(int port){
        ServerBootstrap http = new ServerBootstrap();
        http.option(ChannelOption.SO_BACKLOG, 1024);
        http.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new PipelineFactory(port, null));
                listeners.add(new HttpWsServerListener(http, port));
    }

    static void newServerTcpChannel(String host, int port){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(new InetSocketAddress(host, port));
    
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new TcpHandler());
            }
        });
        listeners.add(new TcpServerListener(serverBootstrap, port));
    }

    static void newNsqConsumer(NsqConfig config){
        Bootstrap clientBootstrap = new Bootstrap();

        clientBootstrap.group(bossGroup);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.remoteAddress(config.socketAddress());
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new NsqConsumerHandler(config));
            }
        });
        listeners.add(new NsqConsumerListener(clientBootstrap, config.port()));
    }

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

    static void start() {
        
        try {
            for (Listener listener : listeners){
                listener.bind();
            }
            for (Listener listener : listeners){
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