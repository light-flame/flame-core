package io.lightflame.nsqconsumer;

import java.util.Queue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * NsqConsumerHandler
 */
public class NsqConsumerHandler extends SimpleChannelInboundHandler<ByteBuf>{

    private MessageProcessing messageProcessing;


    public NsqConsumerHandler(NsqConfig config) {
        this.messageProcessing = new MessageProcessing(config);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        messageProcessing.magic(ctx).sub(ctx);
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception{
        Queue<FrameType> q = messageProcessing
            .addBuffer(in)
            .buildQueue()
            .getQueue();

        while (!q.isEmpty()){
            FrameType f = q.poll();
            f.proccess(ctx);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}