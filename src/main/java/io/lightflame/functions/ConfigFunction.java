package io.lightflame.functions;

import java.util.Optional;
import java.util.function.Function;

import io.lightflame.configuration.Config;

/**
 * ConfigFunction
 */
public interface ConfigFunction extends BeanFunction, Function<Config, Optional<?>> {

    
}