package io.lightflame.http;


/**
 * HttpFunction
 */
@FunctionalInterface
public interface FlameHttpFunction {

    FlameHttpContext chain(FlameHttpContext ctx) throws Exception;
    
}