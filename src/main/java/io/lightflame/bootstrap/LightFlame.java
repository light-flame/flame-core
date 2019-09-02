package io.lightflame.bootstrap;

import java.net.InetSocketAddress;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import io.lightflame.bean.ConfigBeanFactory;
import io.lightflame.bean.HttpBeanFactory;
import io.lightflame.http.WebServer;

/**
 * LightFlame
 */
public class LightFlame {

    private static final Logger LOGGER = Logger.getLogger(LightFlame.class);

    static public void start(Class<?> clazz){     
        BasicConfigurator.configure();   
        LOGGER.info("Light-flame staring at port 8080");
        
        try {
            HttpBeanFactory.create(clazz);
            ConfigBeanFactory.create(clazz);
            Runnable server = new WebServer(new InetSocketAddress(8080));
            while (true) {
                server.run();
                Thread.sleep(5);
            }
        }catch(Exception e){}

    }
}