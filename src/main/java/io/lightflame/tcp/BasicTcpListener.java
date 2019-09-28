package io.lightflame.tcp;

import io.lightflame.bootstrap.Listener;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class BasicTcpListener implements Listener {

    private ServerBootstrap server;
    private Channel ch;
    private int port;

    public BasicTcpListener(String host, int port){
        ServerBootstrap server = new ServerBootstrap();
        server.channel(NioServerSocketChannel.class);
        server.localAddress(new InetSocketAddress(host, port));

        server.childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel socketChannel) {
                socketChannel.pipeline().addLast(new TcpHandler());
            }
        });
        this.server = server;
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
        this.server.group(boss);
        this.ch = this.server.bind(this.port).sync().channel();
    }
}
