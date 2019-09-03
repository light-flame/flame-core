package io.lightflame.functions;

import java.util.function.BiFunction;

import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPResponse;
import io.lightflame.http.HTTPSession;

/**
 * HttpFunction
 */
public interface DefaultHttpFunction extends BiFunction<HTTPSession, HTTPRequest, HTTPResponse>, BeanFunction{

    
}