package io.lightflame.httpAdapters;

import com.google.gson.Gson;

import io.lightflame.functions.HttpInAdapterFunction;
import io.netty.handler.codec.DecoderResult;

/**
 * JsonUnmashallApaptor
 */
public class HttpInAdapter {
    
    public <E> HttpInAdapterFunction<E> jsonUnmarshall(Class<E> clazz) {
        Gson gson = new Gson();
        return (req) -> {
            // StringBuilder buf = new StringBuilder();
            // DecoderResult result = req.decoderResult();
            // if (!result.isSuccess()) {
            //     throw new Exception();
            // }
        
            // buf.append(".. WITH DECODER FAILURE: ");
            // buf.append(result.cause());
            // buf.append("\r\n");

            
            // byte[] b =  s.writeBody();
            // E obj = gson.fromJson(String.valueOf(b), clazz);
            return null;
        };
    }
    
}