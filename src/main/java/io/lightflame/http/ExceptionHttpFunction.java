package io.lightflame.http;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * ExceptionHttpFunction
 */
public interface ExceptionHttpFunction {

    FullHttpResponse call(Exception e);
    
}