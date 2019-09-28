package io.lightflame.http;

import io.lightflame.bootstrap.Listener;
import io.lightflame.bootstrap.PipelineFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class BasicHttpWsListener implements Listener {

    private ServerBootstrap server;
    private Channel chf;
    private int port;

    public BasicHttpWsListener(int port){
        ServerBootstrap http = new ServerBootstrap();
        http.option(ChannelOption.SO_BACKLOG, 1024);
        http.channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new PipelineFactory(port, null));
        this.server = http;
        this.port = port;
    }

    @Override
    public void sync()throws InterruptedException {
        this.chf.closeFuture().sync();
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public void close() {
        this.chf.close();
    }

    @Override
    public void bind(EventLoopGroup boss, EventLoopGroup worker) throws InterruptedException{
        server.group(boss, worker);
        this.chf = server.bind(this.port).sync().channel();
    }
}
