package io.lightflame.nsqconsumer;

import io.lightflame.bootstrap.Flame;

import java.net.InetSocketAddress;

public class BasicNsqConfig implements NsqConfig{

    int port;
    String host;
    String topic;
    String channel;
    Flame<FlameNsqContext, FlameNsqContext> func;

    public BasicNsqConfig(String host, int port, String topic, String channel, Flame<FlameNsqContext, FlameNsqContext> func){
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
    public Flame function() {
        return this.func;
    }

    @Override
    public int rdy() {
        return 2;
    }


}
