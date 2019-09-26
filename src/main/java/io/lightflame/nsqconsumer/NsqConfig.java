package io.lightflame.nsqconsumer;

import java.net.InetSocketAddress;

public interface NsqConfig {
    InetSocketAddress socketAddress();
    int port();
    NsqConsumerHandler handler();
}
