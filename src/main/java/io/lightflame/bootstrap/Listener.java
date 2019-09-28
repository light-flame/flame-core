package io.lightflame.bootstrap;

import io.netty.channel.EventLoopGroup;

public interface Listener {
    void sync() throws InterruptedException;
    int port();
    void close();
    void bind(EventLoopGroup boss, EventLoopGroup worker) throws InterruptedException;
}
