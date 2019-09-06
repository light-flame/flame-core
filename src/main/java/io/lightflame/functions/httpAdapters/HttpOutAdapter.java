package io.lightflame.functions.httpAdapters;

import com.google.gson.Gson;

import io.lightflame.functions.HttpOutAdapterFunction;
import io.lightflame.functions.HttpResponseFunction;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * HttpOutAdapter
 */
public class HttpOutAdapter {

    public <E> HttpOutAdapterFunction<E> jsonMarshall(Class<E> clazz){
        Gson gson = new Gson();
        return (obj) -> {
            String objStr = gson.toJson(obj);
            FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(objStr, CharsetUtil.UTF_8));
            return response;
        };
    }

    public HttpResponseFunction status(HttpResponseStatus status){
        return (response) -> {
            response.setStatus(status);
            return response;
        };
    }


}