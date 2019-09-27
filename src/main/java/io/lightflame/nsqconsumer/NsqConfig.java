package io.lightflame.nsqconsumer;

import java.net.InetSocketAddress;

public interface NsqConfig {
    InetSocketAddress socketAddress();
    int port();
    String channel();
    String topic();
    FlameNsqFunction function();
    int rdy();
}
