package io.lightflame.bootstrap;


import java.net.InetSocketAddress;
import java.util.List;

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
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * NettyConfig
 */
public class NettyConfig {

    private static EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static Channel httpChannel;
    private static ChannelFuture tcpChannel;

    static void newHttpWsListener(int port){
        if (httpChannel != null){
            return;
        }
        try {
            ServerBootstrap http = new ServerBootstrap();
            http.option(ChannelOption.SO_BACKLOG, 1024);
            http.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new PipelineFactory(null));
    
            httpChannel = http.bind(port).sync().channel();
        } catch (Exception e) {
            
        }
    }

    static void newTcpChannel(String host, int port){
        if (tcpChannel != null){
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
            tcpChannel = serverBootstrap.bind().sync();
        }catch (Exception e){
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
                if (httpChannel != null){
                    httpChannel.closeFuture().sync();
                }
                // ch2.closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
            }catch(Exception e){}
       



    }
}