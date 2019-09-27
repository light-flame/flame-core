package io.lightflame.nsqconsumer;

import io.netty.channel.ChannelHandlerContext;

/**
 * FlameNsqCtx
 */
public class FlameNsqCtx {

    private long timeStamp;
    private String msgId;
    private String msg;
    private ChannelHandlerContext ctx;
    private NsqCommands cmds = new NsqCommands();

    FlameNsqCtx(long timeStamp, String msgId, String msg, ChannelHandlerContext ctx) {
        this.timeStamp = timeStamp;
        this.msgId = msgId;
        this.msg = msg;
        this.ctx = ctx;
    }

    void ack(){
        cmds.ack(this.ctx, this.msgId);
    }

    public String getMsg() {
        return msg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}