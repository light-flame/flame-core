package io.lightflame.functions;

import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpInAdapter
 */
public interface HttpInAdapterFunction<E> extends Function<FullHttpRequest, E> {

    
}