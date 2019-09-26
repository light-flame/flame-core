package io.lightflame.nsqconsumer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * FlameNsqCtx
 */
public class FlameNsqCtx {

    private long timeStamp;
    private String msgId;
    private String msg;
    private ChannelHandlerContext ctx;

    FlameNsqCtx(long timeStamp, String msgId, String msg, ChannelHandlerContext ctx) {
        this.timeStamp = timeStamp;
        this.msgId = msgId;
        this.msg = msg;
        this.ctx = ctx;
    }

    public void ack(){
        ctx.writeAndFlush(Unpooled.copiedBuffer(String.format("FIN %s\n", msgId), CharsetUtil.UTF_8));
    }

    public String getMsg() {
        return msg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}