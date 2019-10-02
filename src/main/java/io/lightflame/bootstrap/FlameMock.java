package io.lightflame.bootstrap;

import org.apache.log4j.Logger;
import org.apache.log4j.varia.NullAppender;

import java.util.ArrayList;
import java.util.List;

public class FlameMock implements LightFlame{

    private static final Logger LOGGER = Logger.getLogger(FlameCore.class);
    private Logger rootLogger = Logger.getRootLogger();
    private List<ConfigFunction> configFunctions = new ArrayList<>();

    @Override
    public void start(Class<?> clazz, String... args) {
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new NullAppender());
        }

        for (ConfigFunction cs : configFunctions){
            cs.run();
        }
    }

    @Override
    public FlameMock addListener(Listener listener) {
        LOGGER.info("Running listener");
        return this;
    }

    @Override
    public FlameMock addConfiguration(ConfigFunction configFunction) {
        configFunctions.add(configFunction);
        return this;
    }

    @Override
    public boolean closeChannel(int port) {
        LOGGER.info("channel closed");
        return true;
    }
}
