package io.lightflame.functions;

import java.util.function.Function;

import io.lightflame.http.HTTPResponse;

/**
 * HttpOutAdapter
 */
public interface HttpOutAdapterFunction<E> extends BeanFunction, Function<E, HTTPResponse>{

    
}