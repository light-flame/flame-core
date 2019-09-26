package io.lightflame.nsqconsumer;

import java.util.Queue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * NsqConsumerHandler
 */
public class NsqConsumerHandler extends SimpleChannelInboundHandler<ByteBuf>{

    private BufferManager bufferManager;


    public NsqConsumerHandler(String topic, String channel, FlameNsqFunction f) {
        bufferManager = new BufferManager(topic, channel, f);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        bufferManager.addMagic(ctx);
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        Queue<FrameType> q = bufferManager
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