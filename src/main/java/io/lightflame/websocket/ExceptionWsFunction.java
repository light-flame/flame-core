package io.lightflame.websocket;

/**
 * ExceptionWsFunction
 */
@FunctionalInterface
public interface ExceptionWsFunction {

    void call(Exception e);

}