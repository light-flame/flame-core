package io.lightflame.websocket;


/**
 * HttpFunction
 */
@FunctionalInterface
public interface FlameWsFunction {

    FlameWsContext chain(FlameWsContext ctx) throws Exception;
    
}