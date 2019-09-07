package io.lightflame.functions.httpAdapters;

import java.util.function.Function;

import com.google.gson.Gson;

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

    public <OUT> Function<OUT, FullHttpResponse> jsonMarshall(Class<OUT> clazz){
        Gson gson = new Gson();
        return (obj) -> {
            String objStr = gson.toJson(obj);
            FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.copiedBuffer(objStr, CharsetUtil.UTF_8));
            return response;
        };
    }

    public Function<FullHttpResponse, FullHttpResponse> status(HttpResponseStatus status){
        return (response) -> {
            response.setStatus(status);
            return response;
        };
    }


}