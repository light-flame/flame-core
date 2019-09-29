package io.lightflame.websocket;


import io.netty.channel.ChannelHandlerContext;

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
}