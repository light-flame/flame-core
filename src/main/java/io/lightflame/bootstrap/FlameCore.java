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
import java.util.concurrent.Callable;


public class FlameCore implements LightFlame{

    private static final Logger LOGGER = Logger.getLogger(FlameCore.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<ConfigFunction> configFunctions = new ArrayList<>();

    private ListenerSync listenerSync = new ListenerSync();


    @Override
    public FlameCore addConfiguration(ConfigFunction configFunction){
        configFunctions.add(configFunction);
        return this;
    }

    @Override
    public FlameCore addListener(Listener listener){
        listenerSync.addListener(listener);
        return this;
    }

    @Override
    public boolean closeChannel(int port){
        return listenerSync.close(port);
    }


    @Override
    public void start(Class<?> clazz, String... args) {
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        }

        for (ConfigFunction cs : configFunctions){
            cs.run();
        }
        LOGGER.info("Light Flame ready!");
        listenerSync.startSync();
    }
}