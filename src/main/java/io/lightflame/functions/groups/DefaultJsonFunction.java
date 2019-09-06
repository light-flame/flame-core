package io.lightflame.functions.groups;

import java.util.function.Function;

import io.lightflame.functions.HttpInAdapterFunction;
import io.lightflame.functions.HttpOutAdapterFunction;
import io.lightflame.functions.HttpResponseFunction;
import io.lightflame.functions.httpAdapters.HttpInAdapter;
import io.lightflame.functions.httpAdapters.HttpOutAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * DefaultJsonFunction
 */
public class DefaultJsonFunction {

    static public <I,O> Function<FullHttpRequest,FullHttpResponse> jsonProducerConsumer(
        Class<I> clazzI, 
        Function<I,O> func,
        Class<O> clazzO,
        HttpResponseStatus status
        ){
        HttpInAdapterFunction<I> jsonUnm =  new HttpInAdapter().jsonUnmarshall(clazzI);
        HttpOutAdapterFunction<O> jsonMarsh = new HttpOutAdapter().jsonMarshall(clazzO);
        HttpResponseFunction setStatus = new HttpOutAdapter().status(status);
        return jsonUnm.andThen(func).andThen(jsonMarsh).andThen(setStatus);
    }
}