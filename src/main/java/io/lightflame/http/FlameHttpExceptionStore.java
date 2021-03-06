package io.lightflame.http;

import java.util.HashMap;
import java.util.Map;


import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * DefaultExceptionStore
 */
public class FlameHttpExceptionStore {
    static private Map<Exception, ExceptionHttpFunction> functionMap = new HashMap<>();

    public ExceptionHttpFunction getFunction(Throwable e){
        ExceptionHttpFunction f = functionMap.get(e);
        if (f == null){
            f = defaultFunction();
        }
        return f;
    }

    public void add(Exception e, ExceptionHttpFunction function){
        functionMap.put(e, function);
    }

    private ExceptionHttpFunction defaultFunction(){
        return (e) -> {
            e.printStackTrace();
            return  new DefaultFullHttpResponse(
                HTTP_1_1, BAD_GATEWAY,
                Unpooled.copiedBuffer("", CharsetUtil.UTF_8));
        };
    }
    
}