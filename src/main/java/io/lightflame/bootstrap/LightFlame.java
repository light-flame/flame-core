package io.lightflame.bootstrap;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import io.netty.channel.Channel;

/**
 * LightFlame
 */
public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<Channel> channels = new ArrayList<>();

    public LightFlame runConfiguration(ConfigFunction configFunction, Config config){
        configFunction.setup(config);
        return this;
    }


    public LightFlame addBasicLog4jConfig(){
        BasicConfigurator.configure();
        return this;
    }

    public LightFlame addChannel(Channel ch){
        this.channels.add(ch);
        return this;
    }
    
    static public Channel newHttpChannel(int port){
        return NettyConfig.newHttpChannel(port);
    } 

    public void start(Class<?> clazz) {     
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        } 
        
        LOGGER.info("Light-flame staring at port 8080");
        NettyConfig.start(channels);
    
    }
}