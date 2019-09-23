package io.lightflame.bootstrap;


import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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

    static Channel newHttpChannel(int port){
        try {
            ServerBootstrap http = new ServerBootstrap();
            http.option(ChannelOption.SO_BACKLOG, 1024);
            http.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new PipelineFactory(null));
    
            return http.bind(port).sync().channel();
        } catch (Exception e) {
            
        }
        return null;
    }

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", SSL ? "8443" : "8080"));

    static void start(List<Channel> channels) {
        
        SslContext sslCtx;
        try {
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            try {
                channels.get(0).closeFuture().sync();
                // ch2.closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
            }catch(Exception e){}
       



    }
}