package io.lightflame.httpAdaptors;

import com.google.gson.Gson;

import io.lightflame.functions.HttpInAdapter;

/**
 * JsonUnmashallApaptor
 */
public class InAdaper<E> {
    Gson gson = new Gson();
    Class<E> clazz;
    


    public HttpInAdapter<E> jsonUnmarshall() {
        return (s, r) -> {
            byte[] b =  s.writeBody();
            E obj = gson.fromJson(String.valueOf(b), clazz);
            return obj;
        };
    }
    
}