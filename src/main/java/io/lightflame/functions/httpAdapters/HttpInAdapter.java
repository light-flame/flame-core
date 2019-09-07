package io.lightflame.functions.httpAdapters;

import java.util.function.Function;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

/**
 * JsonUnmashallApaptor
 */
public class HttpInAdapter {

    private <IN> Function<FullHttpRequest,Function<FullHttpRequest, IN>> checkContentType(Function<FullHttpRequest, IN> jsonUnm, Function<FullHttpRequest, IN> textUnm){
        return (req) -> {
            HttpHeaders headers = req.headers();
            if (headers.get(HttpHeaderNames.CONTENT_TYPE) == "application/json"){
                return jsonUnm;
            }
            return textUnm;
        };
    }

    public <IN> Function<FullHttpRequest, IN> unmashallGatewayOut(Function<FullHttpRequest, IN> jsonUnm, Function<FullHttpRequest, IN> formUnm){
        return (req) -> {
            Function<FullHttpRequest, IN> f = checkContentType(jsonUnm, formUnm).apply(req);
            IN obj = f.apply(req);
            return obj;
        };
    }
    
    
    public <IN> Function<FullHttpRequest, IN> jsonUnmarshall(Class<IN> clazz){
        Gson gson = new Gson();
        return (req) -> {
            String jsonStr = req.content().toString(CharsetUtil.UTF_8);
            IN obj = gson.fromJson(jsonStr, clazz);
            return obj;
        };
    }
    
}