package io.lightflame.functions;

import java.util.function.BiFunction;

import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPResponse;
import io.lightflame.http.HTTPSession;

/**
 * HttpFunction
 */
public interface HttpFunction extends BiFunction<HTTPSession, HTTPRequest, HTTPResponse>, BeanFunction{

    
}