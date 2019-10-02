package io.lightflame.bootstrap;

public interface LightFlame {
    void start(Class<?> clazz, String... args);
    LightFlame addListener(Listener listener);
    LightFlame addConfiguration(ConfigFunction configFunction);
    boolean closeChannel(int port);
}
