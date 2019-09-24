package io.lightflame.bootstrap;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;


/**
 * LightFlame
 */
public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);
    private Logger rootLogger = Logger.getRootLogger();

    public LightFlame runConfiguration(ConfigFunction configFunction, Config config){
        configFunction.setup(config);
        return this;
    }


    public LightFlame addBasicLog4jConfig(){
        BasicConfigurator.configure();
        return this;
    }

    
    public LightFlame addHttpAndWsListener(int port){
        NettyConfig.newHttpWsListener(port);
        return this;
    } 

    public LightFlame addTcpServerListener(String host, int port){
        NettyConfig.newTcpChannel(host, port);
        return this;
    } 

    public void start(Class<?> clazz) {     
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        } 
        
        LOGGER.info("Light-flame staring at port 8080");
        NettyConfig.start();
    
    }
}