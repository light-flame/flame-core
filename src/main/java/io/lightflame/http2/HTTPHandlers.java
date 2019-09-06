package io.lightflame.http2;

import java.util.function.Function;

import io.lightflame.bean.NettyHttpStore;
import io.lightflame.functions.NettyHttpFunction;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

    public FullHttpResponse getHandle(HttpRequest request){
        
        Function<HttpRequest, FullHttpResponse> function = new NettyHttpStore()
            .getFunctionByRequest(request);
        if (function == null) {
            function = handler404();
        }
        return function.apply(request);
    }


    NettyHttpFunction handler404() {
        return (req) -> {
            FullHttpResponse response = new DefaultFullHttpResponse(
                req.protocolVersion(), 
                OK,
                Unpooled.wrappedBuffer("nothing here.. =(".getBytes()));
            response.headers()
                    .set(CONTENT_TYPE, TEXT_PLAIN)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());
            return response;
        };
    }

}