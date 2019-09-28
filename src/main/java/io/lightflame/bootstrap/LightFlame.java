package io.lightflame.bootstrap;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<ConfigStore> configsStore = new ArrayList<>();

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Map<Integer, Listener> listeners = new HashMap<>();

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


    public LightFlame addListener(Listener listener){
        this.listeners.put(listener.port(), listener);
        return this;
    }

    public LightFlame runListener(Listener listener){
        try{
            listener.bind(bossGroup,workerGroup);
        }catch (InterruptedException e){
            exit();
        }
        return this;
    }

    public void removeListener(int port){
        Listener l = this.listeners.remove(port);
        l.close();
    }

//    public void closeChannel(int port){
//        this.netty.closeListenerByPort(port);
//    }

//    public void openChannel(int port, NettyConfig.ListenerKind kind){
//        this.netty.openListener(port, kind);
//    }

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

        try {
            for (Listener listener : listeners.values()){
                listener.bind(bossGroup, workerGroup);
            }
            LOGGER.info("Light Flame ready!");
            for (Listener listener : listeners.values()){
                listener.sync();
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            exit();
        }
    }

    public void exit(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}