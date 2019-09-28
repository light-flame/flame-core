package io.lightflame.bootstrap;


import io.lightflame.nsqconsumer.NsqConfig;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import java.util.ArrayList;
import java.util.List;


public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<ConfigStore> configsStore = new ArrayList<>();
    private NettyConfig netty = new NettyConfig();

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
        this.netty.newHttpWsListener(port);
        return this;
    } 

    public LightFlame addNsqConsumer(NsqConfig conf){
        this.netty.newNsqConsumer(conf);
        return this;
    } 

    public LightFlame addTcpServerListener(String host, int port){
        this.netty.newServerTcpChannel(host, port);
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

    public void closeChannel(int port){
        this.netty.closeListenerByPort(port);
    }

//    public void openChannel(int port, NettyConfig.ListenerKind kind){
//        this.netty.openListener(port, kind);
//    }

    public void start(Class<?> clazz) {     
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        }

        for (ConfigStore cs : configsStore){
            cs.func.setup(cs.conf);
        }
        
        LOGGER.info("Light Flame ready!");
        netty.start();
    }
}