package io.lightflame.nsqconsumer;

import java.util.LinkedList;
import java.util.Queue;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageAggregationException;
import io.netty.util.CharsetUtil;

/**
 * BufferManager
 */
public class MessageProcessing {

    private Integer size;
    private Integer frameType;
    private ByteBuf buffer = Unpooled.buffer(0);
    private Queue<FrameType> frames = new LinkedList<>();
    private NsqStep currentStep = NsqStep.MAGIC;
    private NsqConfig config;
    private NsqCommands cmds = new NsqCommands();

    public MessageProcessing(NsqConfig config) {
        this.config = config;
    }

    public MessageProcessing magic(ChannelHandlerContext ctx){
        cmds.magic(ctx);
        return this;
    }

    public MessageProcessing sub(ChannelHandlerContext ctx){
        cmds.sub(ctx, config);
        this.currentStep = NsqStep.SUB;
        return this;
    }

    enum NsqStep{
        MAGIC,SUB,RDY
    }

    
    enum FrameTypeEnum {
        RESPONSE(0), MESSAGE(2);

        private final int value;
        private FrameTypeEnum(int value) {
            this.value = value;
        }
    }
    
    class FrameTypeResponse implements FrameType{
        private ByteBuf msgBuffer;

        FrameTypeResponse(ByteBuf b){
            this.msgBuffer = b;
        }

        @Override
        public void proccess(ChannelHandlerContext ctx) {
            String finalMsg = msgBuffer.toString(CharsetUtil.UTF_8);
            if (finalMsg.equals("OK") && currentStep == NsqStep.SUB){
                cmds.rdy(ctx, config);
                return;
            }
            if (finalMsg.equals("_heartbeat_")){
                cmds.nop(ctx);
                return;
            }
        }
    }

    class FrameTypeMessage implements FrameType{
        private ByteBuf msgBuf;

        FrameTypeMessage(ByteBuf b){
            this.msgBuf = b;
        }

        @Override
        public void proccess(ChannelHandlerContext ctx) throws Exception{
            long ts =  msgBuf.readBytes(8).readLong();
            msgBuf.readBytes(2);
            String msgId =  msgBuf.readBytes(16).toString(CharsetUtil.UTF_8);
            String msg =  msgBuf.toString(CharsetUtil.UTF_8);

            try {
                config.function().apply(new FlameNsqContext(ts, msgId, msg, ctx));
                cmds.ack(ctx, msgId);
            }catch (Exception e){
                throw new Exception(e);
            }
        }
    }

    private int messageSize(){
        return this.size-4;
    }


    public MessageProcessing addBuffer(ByteBuf in){
        this.buffer = Unpooled.copiedBuffer(buffer, in);
        return this;
    }

    public MessageProcessing buildQueue(){
        prepareMessage();
        return this;
    }

    private void prepareMessage(){
        if (this.buffer.readableBytes() < 8){
            return;
        }
        if (size == null){
            this.size =  this.buffer.readBytes(4).readInt();
            this.frameType =  this.buffer.readBytes(4).readInt();
        }
        if (this.buffer.readableBytes() < messageSize()){
            return;
        }

        ByteBuf msgBuf =  buffer.readBytes(messageSize()).copy();

        if (this.frameType == FrameTypeEnum.RESPONSE.value){
            this.frames.add(new FrameTypeResponse(msgBuf));
            this.size = null;
            this.prepareMessage();
            return;
        }
        if (this.frameType == FrameTypeEnum.MESSAGE.value){
            this.frames.add(new FrameTypeMessage(msgBuf));
            this.size = null;
            this.prepareMessage();
            return;
        }

        throw new MessageAggregationException("error making msg");
    }

    public Queue<FrameType> getQueue(){
        return this.frames;
    }

    
}