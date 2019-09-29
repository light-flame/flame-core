package io.lightflame.bootstrap;

import org.apache.log4j.BasicConfigurator;

public class FlameLog4jConfig {

    public ConfigFunction basicConfig() {
        return BasicConfigurator::configure;
    }
}
