package io.lightflame.nsqconsumer;

import java.net.InetSocketAddress;

public class BasicNsqConfig implements NsqConfig{

    int port;
    String host;
    String topic;
    String channel;
    FlameNsqFunction func;

    public BasicNsqConfig(String host, int port, String topic, String channel, FlameNsqFunction func){
        this.port = port;
        this.host = host;
        this.channel = channel;
        this.topic = topic;
        this.func = func;
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
    public String channel() {
        return this.channel;
    }

    @Override
    public String topic() {
        return this.topic;
    }

    @Override
    public FlameNsqFunction function() {
        return this.func;
    }

    @Override
    public int rdy() {
        return 2;
    }


}
