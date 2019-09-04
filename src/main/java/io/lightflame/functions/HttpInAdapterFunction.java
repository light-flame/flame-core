package io.lightflame.functions;

import java.util.function.Function;

import io.lightflame.http.HTTPSession;

/**
 * HttpInAdapter
 */
public interface HttpInAdapterFunction<E> extends Function<HTTPSession, E> {

    
}