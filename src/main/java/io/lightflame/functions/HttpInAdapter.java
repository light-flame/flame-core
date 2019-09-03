package io.lightflame.functions;

import java.util.function.BiFunction;

import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPSession;

/**
 * HttpInAdapter
 */
public interface HttpInAdapter<E> extends BiFunction<HTTPSession, HTTPRequest, E> {

    
}