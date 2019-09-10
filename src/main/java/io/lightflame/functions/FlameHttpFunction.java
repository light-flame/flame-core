package io.lightflame.functions;

import io.lightflame.context.FlameHttpContext;

/**
 * HttpFunction
 */
@FunctionalInterface
public interface FlameHttpFunction {

    FlameHttpContext chain(FlameHttpContext ctx);
    
}