
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

    private final AttributeKey<String> uriAttKey = AttributeKey.valueOf("request.uri");
    private final AttributeKey<HttpHeaders> headersAttrKey = AttributeKey.valueOf("request.headers");
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
            ctx.channel().attr(uriAttKey).set(uri);
            ctx.channel().attr(headersAttrKey).set(headers);

        }else{
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled
        if (frame instanceof TextWebSocketFrame) {
            String uri = ctx.channel().attr(uriAttKey).get();
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();

            WsRequestWrapper wrapper = new WsRequestWrapper(request, uri, this.port);

            try {
                FlameWsResponse response = new FlameWsStore().runFunctionByRequest(wrapper);
                ctx.channel().writeAndFlush(new TextWebSocketFrame(response.getResponse()));
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
