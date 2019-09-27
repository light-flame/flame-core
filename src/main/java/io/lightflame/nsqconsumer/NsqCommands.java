package io.lightflame.nsqconsumer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

public class NsqCommands {

    NsqCommands() {
    }

    void magic(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("  V2", CharsetUtil.UTF_8));
    }

    void sub(ChannelHandlerContext ctx, NsqConfig config){
        String sub = String.format("%s %s %s\n", "SUB", config.topic(), config.channel());
        ctx.writeAndFlush(Unpooled.copiedBuffer(sub, CharsetUtil.UTF_8));
    }

    void rdy(ChannelHandlerContext ctx, NsqConfig config){
        ctx.writeAndFlush(Unpooled.copiedBuffer(String.format("RDY %d\n", config.rdy()), CharsetUtil.UTF_8));
    }

    void nop(ChannelHandlerContext ctx){
        ctx.writeAndFlush(Unpooled.copiedBuffer("NOP\n", CharsetUtil.UTF_8));
    }

    void ack(ChannelHandlerContext ctx, String msgId){
        ctx.writeAndFlush(Unpooled.copiedBuffer(String.format("FIN %s\n", msgId), CharsetUtil.UTF_8));
    }
}
