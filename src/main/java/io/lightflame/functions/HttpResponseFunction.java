package io.lightflame.functions;

import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * HttpResponseFunction
 */
public interface HttpResponseFunction extends BeanFunction, Function<FullHttpResponse, FullHttpResponse> {

    
}