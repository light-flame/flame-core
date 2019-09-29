package io.lightflame.bootstrap;

public interface LightFlame {
    void start(Class<?> clazz, String... args);
    void runListener(Listener listener);
    LightFlame addConfiguration(ConfigFunction configFunction);
    void closeChannel(int port);
}
