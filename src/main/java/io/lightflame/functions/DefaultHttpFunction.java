package io.lightflame.functions;

import java.util.function.Function;

import io.lightflame.http.HTTPResponse;
import io.lightflame.http.HTTPSession;

/**
 * HttpFunction
 */
public interface DefaultHttpFunction extends Function<HTTPSession, HTTPResponse>, BeanFunction{

    
}