package io.lightflame.bootstrap;

import java.util.Optional;

import io.lightflame.bootstrap.Config;

/**
 * ConfigFunction
 */
@FunctionalInterface
public interface ConfigFunction {

    Optional<?> setup(Config conf);
}