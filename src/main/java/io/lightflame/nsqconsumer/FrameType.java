package io.lightflame.nsqconsumer;

import io.netty.channel.ChannelHandlerContext;

public interface FrameType {
    void proccess(ChannelHandlerContext ctx) throws Exception;
}
