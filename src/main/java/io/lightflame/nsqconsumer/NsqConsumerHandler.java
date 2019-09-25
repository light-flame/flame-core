package io.lightflame.nsqconsumer;

import java.nio.ByteBuffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
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
        ctx.writeAndFlush(Unpooled.copiedBuffer("  V2", CharsetUtil.UTF_8));

        String sub = String.format("%s %s %s\n", "SUB", "write_test1", "ch");
        ctx.writeAndFlush(Unpooled.copiedBuffer(sub, CharsetUtil.UTF_8));

    }

    private Integer size;
    private Integer frameType;
    private ByteBuf buffer = Unpooled.buffer(0);


    @Override
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        buffer = Unpooled.copiedBuffer(buffer, in);
        while (buffer.readerIndex() != buffer.writerIndex()){

            System.out.println("0 " + buffer.readerIndex() + " "+ buffer.writerIndex());

            if ((buffer.writerIndex() - buffer.readerIndex()) < 8){
                break;
            }

            if (size == null){
                size =  buffer.readBytes(4).readInt();
                frameType =  buffer.readBytes(4).readInt();
            }


            if ((buffer.writerIndex() - buffer.readerIndex()) < size-4){
                break;
            }
            ByteBuf msgBuf =  buffer.readBytes(size-4).copy();

            System.out.println("1");
           

            if (frameType == 0){
                String finalMsg = msgBuf.toString(CharsetUtil.UTF_8);
                size = null;
                if (finalMsg.equals("OK")){
                    ctx.writeAndFlush(Unpooled.copiedBuffer("RDY 100\n", CharsetUtil.UTF_8));
                    return;
                }
                if (finalMsg.equals("_heartbeat_")){
                    // ctx.writeAndFlush(Unpooled.copiedBuffer("RDY 1\n", CharsetUtil.UTF_8));
                    ctx.writeAndFlush(Unpooled.copiedBuffer("NOP\n", CharsetUtil.UTF_8));
                    return;
                }
            }

            long ts =  msgBuf.readBytes(8).readLong();
            msgBuf.readBytes(2);
            String msgId =  msgBuf.readBytes(16).toString(CharsetUtil.UTF_8);

            System.out.println("2");

            String finalMsg =  msgBuf.toString(CharsetUtil.UTF_8);
    
    
            ctx.writeAndFlush(Unpooled.copiedBuffer(String.format("FIN %s\n", msgId), CharsetUtil.UTF_8));
            System.out.println("Client received: " + finalMsg);
            size = null;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        cause.printStackTrace();
        ctx.close();
    }
}