package io.lightflame.nsqconsumer;

import io.lightflame.bootstrap.Listener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class BasicNsqConsumerListener implements Listener {

    private Bootstrap bs;
    private Channel ch;
    private int port;

    public BasicNsqConsumerListener(NsqConfig config){
        Bootstrap clientBootstrap = new Bootstrap();

        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.remoteAddress(config.socketAddress());
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new NsqConsumerHandler(config));
            }
        });
        this.bs = clientBootstrap;
        this.port = config.port();
    }

    @Override
    public void sync() throws InterruptedException{
        this.ch.closeFuture().sync();
    }

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public void close() {
        this.ch.close();
    }

    @Override
    public void bind(EventLoopGroup boss, EventLoopGroup worker) throws InterruptedException {
        this.bs.group(boss);
        this.bs.connect().sync();
    }
}
