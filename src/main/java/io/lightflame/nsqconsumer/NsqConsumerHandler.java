package io.lightflame.nsqconsumer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * NsqConsumerHandler
 */
public class NsqConsumerHandler extends SimpleChannelInboundHandler<ByteBuf>{

    private BufferManager bufferManager;

    public NsqConsumerHandler(String topic, String channel) {
        bufferManager = new BufferManager(topic, channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        bufferManager.addMagic(ctx);
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        bufferManager
            .addBuffer(in)
            .prepareMessages()
            .proccessMessages(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}