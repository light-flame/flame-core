package io.lightflame.bean;

import java.lang.reflect.Method;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import io.lightflame.annotations.Endpoint;
import io.lightflame.annotations.Handler;
import io.lightflame.http.HTTPHandlers;
import io.lightflame.http.HTTPRequest;
import io.lightflame.http.HTTPSession;

/**
 * BeanFactory
 */
public class HttpBeanFactory {

    private static final Logger LOGGER = Logger.getLogger(HttpBeanFactory.class);

    static public void create(Class<?> mainCLass) throws Exception {
        addCustom404();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(mainCLass.getPackageName()))
            .setScanners(new SubTypesScanner(), new MethodAnnotationsScanner(), new TypeAnnotationsScanner())
        );
        
        Set<Class<?>> setClazzes = reflections.getTypesAnnotatedWith(Endpoint.class);
        Set<Method> setMethods = reflections.getMethodsAnnotatedWith(Handler.class);

        for (Class<?> clazz : setClazzes){
            HttpBeanStore.addHttpBean(new Bean<>(clazz));
        }

        for (Method method : setMethods){
            String url = "";
            Class<?> clazz = method.getDeclaringClass();

            Endpoint endpointAnn =  clazz.getAnnotation(Endpoint.class);
            url = endpointAnn == null ? "/" : endpointAnn.value();

            Handler handlerAnn = method.getAnnotation(Handler.class);
            url += handlerAnn.value();
            HttpBeanStore.addBeanMethod(clazz, url, method);
            LOGGER.info("registering url at: " + url);
        }
    }

    static public void addCustom404()throws Exception{
        Method m = HTTPHandlers.class.getMethod("handler404", HTTPSession.class, HTTPRequest.class);
        HttpBeanStore.addHttpBean(new Bean<>(HTTPHandlers.class));
        HttpBeanStore.addBeanMethod(HTTPHandlers.class, "404", m);
    }
    
}