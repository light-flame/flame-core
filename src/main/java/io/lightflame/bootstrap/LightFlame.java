package io.lightflame.bootstrap;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import io.lightflame.functions.ConfigFunction;

/**
 * LightFlame
 */
public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);

    public LightFlame runConfiguration(ConfigFunction configFunction, Config config){
        configFunction.setup(config);
        return this;
    }

    public void start(Class<?> clazz) {     
        BasicConfigurator.configure();   
        LOGGER.info("Light-flame staring at port 8080");
        WebConfig.start(8080);
    
    }
}