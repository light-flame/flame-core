package io.lightflame.functions;

import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * ExceptionHttpFunction
 */
public interface ExceptionHttpFunction extends Function<Exception, FullHttpResponse>, BeanFunction {

    
}