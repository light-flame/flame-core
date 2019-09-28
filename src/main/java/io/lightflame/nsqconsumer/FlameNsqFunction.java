package io.lightflame.nsqconsumer;

@FunctionalInterface
public interface FlameNsqFunction {
    FlameNsqContext chain(FlameNsqContext ctx) throws Exception;
}
