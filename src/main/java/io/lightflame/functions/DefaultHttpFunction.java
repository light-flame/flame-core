package io.lightflame.functions;

import java.util.function.Function;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;


/**
 * HttpFunction
 */
public interface DefaultHttpFunction extends Function<FullHttpRequest, FullHttpResponse>, BeanFunction {

    
}