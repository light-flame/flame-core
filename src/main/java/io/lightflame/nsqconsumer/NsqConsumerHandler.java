package io.lightflame.nsqconsumer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * NsqConsumerHandler
 */
public class NsqConsumerHandler extends SimpleChannelInboundHandler<ByteBuf>{

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("sending to client!!!!!!");
        ctx.writeAndFlush(Unpooled.copiedBuffer("  V2", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        String msg = in.toString(CharsetUtil.UTF_8);
        System.out.println(msg);
        if (msg.contains("heartbeat")){
            System.out.println("sending NOP");
            ctx.writeAndFlush(Unpooled.copiedBuffer("NOP\n", CharsetUtil.UTF_8));
        }
        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}