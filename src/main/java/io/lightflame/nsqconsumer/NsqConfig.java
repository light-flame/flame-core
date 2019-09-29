package io.lightflame.nsqconsumer;

import io.lightflame.bootstrap.Flame;

import java.net.InetSocketAddress;
import java.util.Optional;

public interface NsqConfig {
    InetSocketAddress socketAddress();
    int port();
    String channel();
    String topic();
    Flame<FlameNsqContext, Optional<Void>> function();
    int rdy();
}
