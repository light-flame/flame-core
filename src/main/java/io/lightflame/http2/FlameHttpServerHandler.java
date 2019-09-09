/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.lightflame.http2;

import io.lightflame.store.FlameExceptionStore;
import io.lightflame.store.FlameHttpStore;
import io.lightflame.context.FlameHttpContext;
import io.lightflame.functions.ExceptionHttpFunction;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

public class FlameHttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private FullHttpRequest request;
    FullHttpResponse response;

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = this.request = (FullHttpRequest) msg;

            if (HttpUtil.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }
            appendDecoderResult(request);
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;

            ByteBuf content = httpContent.content();
            if (content.isReadable()) {
                request.replace(content);
                appendDecoderResult(request);
            }

            if (msg instanceof LastHttpContent) {
                // end of content

                LastHttpContent trailer = (LastHttpContent) msg;
               
                
                if (!trailer.trailingHeaders().isEmpty()) {
                    for (CharSequence name: trailer.trailingHeaders().names()) {
                        for (CharSequence value: trailer.trailingHeaders().getAll(name)) {
                            request.headers().set(name, value);
                        }
                    }
                }

                if (!writeResponse(trailer, ctx)) {
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                }
            }
        }
    }

    private static void appendDecoderResult(HttpObject o)throws Exception {
        DecoderResult result = o.decoderResult();
        if (result.isSuccess()) {
            return;
        }
        throw new Exception("error decoding", result.cause());
    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx)throws Exception {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpUtil.isKeepAlive(request);

        // Build the response object.
        
        // FullHttpResponse response = new DefaultFullHttpResponse(
        //         HTTP_1_1, currentObj.decoderResult().isSuccess()? OK : BAD_REQUEST,
        //         Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

        // response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        try {
            FlameHttpContext flameCtx = new FlameHttpStore().runFunctionByRequest(request);
            response = flameCtx.getResponse();
        }catch(Exception e){
            ExceptionHttpFunction fExc =  new FlameExceptionStore().getFunction(e);
            response = fExc.apply(e);
        }finally{
            writeOnEnd(ctx, keepAlive);
        }
        return keepAlive;
    }

    private void writeOnEnd(ChannelHandlerContext ctx, boolean keepAlive){
        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // Encode the cookie.
        String cookieString = request.headers().get(HttpHeaderNames.COOKIE);
        if (cookieString != null) {
            Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                for (Cookie cookie: cookies) {
                    response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
                }
            }
        } else {
            // Browser sent no cookie.  Add some.
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("key1", "value1"));
            response.headers().add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode("key2", "value2"));
        }

        // Write the response.
        ctx.write(response);
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE, Unpooled.EMPTY_BUFFER);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
