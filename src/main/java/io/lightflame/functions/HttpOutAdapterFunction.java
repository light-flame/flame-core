package io.lightflame.functions;

import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * HttpOutAdapter
 */
public interface HttpOutAdapterFunction<E> extends BeanFunction, Function<E, FullHttpResponse>{

    
}