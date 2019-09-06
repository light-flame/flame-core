package io.lightflame.httpAdapters;

import java.io.IOException;
import java.io.UncheckedIOException;

import com.google.gson.Gson;

import io.lightflame.functions.HttpInAdapterFunction;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

/**
 * JsonUnmashallApaptor
 */
public class HttpInAdapter {
    
    public <E> HttpInAdapterFunction<E> jsonUnmarshall(Class<E> clazz){
        Gson gson = new Gson();
        return (req) -> {
            try {
                HttpHeaders headers = req.headers();
                if (headers.get(HttpHeaderNames.CONTENT_TYPE) != "application/json"){
                    throw new IOException("content type not accepted");
                }
                String jsonStr = req.content().toString(CharsetUtil.UTF_8);
                E obj = gson.fromJson(jsonStr, clazz);
                return obj;
            }catch(IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
    
}