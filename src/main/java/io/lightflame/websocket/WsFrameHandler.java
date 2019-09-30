
package io.lightflame.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete;
import io.netty.util.AttributeKey;

/**
 * Echoes uppercase content of text frames.
 */
public class WsFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private int port;

    public WsFrameHandler(int port){
        this.port = port;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof HandshakeComplete){
            HandshakeComplete handshake = (HandshakeComplete)evt;

            HttpHeaders headers = handshake.requestHeaders();
            String uri = handshake.requestUri();

            //put to channel context
            ctx.channel().attr(WsAttributes.uriAttKey).set(uri);
            ctx.channel().attr(WsAttributes.headersAttrKey).set(headers);
            new FlameWs().addChannelToSession(headers.get("Sec-WebSocket-Key"),ctx);

        }else{
            ctx.fireUserEventTriggered(evt);
        }
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled
        if (frame instanceof TextWebSocketFrame) {

            // Send the uppercase string back.
            System.out.println("msg: " + (frame));
            String message = ((TextWebSocketFrame) frame).text();
            System.out.println("msg: " + message);
            ctx.channel().attr(WsAttributes.requestAttKey).set(message);
            ctx.channel().attr(WsAttributes.portAttKey).set(String.valueOf(port));

            try {
                FlameWsContext response = new FlameWs().runFunctionByRequest(ctx);
            }catch(Exception e){
                ExceptionWsFunction fExc =  new FlameWsExceptionStore().getFunction(e);
                fExc.call(e);
            }



        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
}
