package io.lightflame.nsqconsumer;

@FunctionalInterface
public interface FlameNsqFunction {
    FlameNsqCtx chain(FlameNsqCtx ctx) throws Exception;
}
