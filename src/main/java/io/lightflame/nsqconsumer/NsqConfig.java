package io.lightflame.nsqconsumer;

import io.lightflame.bootstrap.Flame;

import java.net.InetSocketAddress;

public interface NsqConfig {
    InetSocketAddress socketAddress();
    int port();
    String channel();
    String topic();
    Flame<FlameNsqContext, FlameNsqContext> function();
    int rdy();
}
