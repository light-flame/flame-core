package io.lightflame.functions;

import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

/**
 * NettyHttpFunction
 */
public interface NettyHttpFunction extends Function<HttpRequest, FullHttpResponse>, BeanFunction {

    
}