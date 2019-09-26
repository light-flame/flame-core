package io.lightflame.bootstrap;


import io.lightflame.nsqconsumer.NsqConfig;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import java.util.ArrayList;
import java.util.List;


/**
 * LightFlame
 */
public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<ConfigStore> configsStore = new ArrayList<>();

    class ConfigStore{
        private ConfigFunction func;
        private Config conf;

        ConfigStore(ConfigFunction f, Config c){
            this.func = f;
            this.conf = c;
        }
    }


    public LightFlame addConfiguration(ConfigFunction configFunction, Config config){
        configsStore.add(new ConfigStore(configFunction, config));
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

    public LightFlame addNsqConsumer(NsqConfig conf){
        NettyConfig.newNsqConsumer(conf);
        return this;
    } 

    public LightFlame addTcpServerListener(String host, int port){
        NettyConfig.newServerTcpChannel(host, port);
        return this;
    }

    public void startMock(Class<?> clazz) {
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        }

        for (ConfigStore cs : configsStore){
            cs.func.setup(cs.conf);
        }
    }

    public void start(Class<?> clazz) {     
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        }

        for (ConfigStore cs : configsStore){
            cs.func.setup(cs.conf);
        }
        
        LOGGER.info("Light-flame staring!");
        NettyConfig.start();
    }
}