package io.lightflame.bootstrap;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * LightFlame
 */
public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);
    private int port = 8080;

    public LightFlame runConfiguration(ConfigFunction configFunction, Config config){
        configFunction.setup(config);
        return this;
    }

    public LightFlame port(int p){
        this.port = p;
        return this;
    }

    public void start(Class<?> clazz) {     
        BasicConfigurator.configure();   
        LOGGER.info("Light-flame staring at port 8080");
        WebConfig.start(port);
    
    }
}