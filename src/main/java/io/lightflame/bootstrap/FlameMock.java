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
    public void runListener(Listener listener) {
        LOGGER.info("Running listener");
    }

    @Override
    public FlameMock addConfiguration(ConfigFunction configFunction) {
        configFunctions.add(configFunction);
        return this;
    }
}
