package io.lightflame.functions;

/**
 * FlameWebSocketFunction
 */
@FunctionalInterface
public interface FlameWebSocketFunction {

    FlameWebSocketFunction chain(FlameWebSocketFunction ctx) throws Exception;

    
}