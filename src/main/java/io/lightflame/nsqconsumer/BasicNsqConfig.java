package io.lightflame.nsqconsumer;

import java.net.InetSocketAddress;

public class BasicNsqConfig implements NsqConfig{

    private int port;
    private String host;
    private String topic;
    private String channel;

    public BasicNsqConfig(String host, int port, String topic, String channel){
        this.port = port;
        this.host = host;
        this.channel = channel;
        this.topic = topic;
    }

    @Override
    public InetSocketAddress socketAddress() {
        return new InetSocketAddress(this.host, this.port);
    }

    @Override
    public int port() {
        return this.port;
    }

    @Override
    public NsqConsumerHandler handler() {
        return new NsqConsumerHandler(this.topic, this.channel);
    }
}
