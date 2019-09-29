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


public class FlameCore implements LightFlame{

    private static final Logger LOGGER = Logger.getLogger(FlameCore.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<ConfigFunction> configFunctions = new ArrayList<>();

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Map<Integer, Listener> listeners = new HashMap<>();


    @Override
    public FlameCore addConfiguration(ConfigFunction configFunction){
        configFunctions.add(configFunction);
        return this;
    }

    public FlameCore addListener(Listener listener){
        this.listeners.put(listener.port(), listener);
        return this;
    }

    @Override
    public void runListener(Listener listener){
        try{
            listener.bind(bossGroup,workerGroup);
        }catch (InterruptedException e){
            exit();
        }
    }

    public void removeListener(int port){
        Listener l = this.listeners.remove(port);
        l.close();
    }

    @Override
    public void closeChannel(int port){
        listeners.get(port).close();
    }

//    public void openChannel(int port, NettyConfig.ListenerKind kind){
//        this.netty.openListener(port, kind);
//    }


    @Override
    public void start(Class<?> clazz, String... args) {
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        }

        for (ConfigFunction cs : configFunctions){
            cs.run();
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