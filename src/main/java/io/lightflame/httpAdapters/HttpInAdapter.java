package io.lightflame.httpAdapters;

import com.google.gson.Gson;

import io.lightflame.functions.HttpInAdapterFunction;

/**
 * JsonUnmashallApaptor
 */
public class HttpInAdapter {
    
    public <E> HttpInAdapterFunction<E> jsonUnmarshall(Class<E> clazz) {
        Gson gson = new Gson();
        return (s) -> {
            byte[] b =  s.writeBody();
            E obj = gson.fromJson(String.valueOf(b), clazz);
            return obj;
        };
    }
    
}