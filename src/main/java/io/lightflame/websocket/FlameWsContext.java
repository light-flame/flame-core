package io.lightflame.websocket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Collection;

/**
 * FlameWebSocketCtx
 */
public class FlameWsContext {

    private ChannelHandlerContext ctx;
    private Session session;


    FlameWsContext(ChannelHandlerContext ctx, Session session){
        this.ctx = ctx;
        this.session = session;
    }

    public String message() {
        return ctx.channel().attr(WsAttributes.requestAttKey).get();
    }

    public void writeToChannel(String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    public void writeToAllChannels(String msg) {
        for (ChannelHandlerContext ctx : session.getAllSessions().values()){
            ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
        }
    }

}