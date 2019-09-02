package io.lightflame.http;

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
import io.lightflame.bean.Bean;
import io.lightflame.bean.HttpBeanStore;

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

    private static final Logger LOGGER = Logger.getLogger(HTTPHandlers.class);

    public HTTPResponse getHandle(HTTPSession session, HTTPRequest request) throws Exception{
        String location = request.getLocation();
        Bean<?> bean = HttpBeanStore.getBeanByURL(location);
        if (bean == null) {
            bean = HttpBeanStore.getBean(HTTPHandlers.class);
            location = "404";
        }
        Method m =bean.getMethod(location);
        Object rv = m.invoke(bean.getInstance(), session, request);  
        return (HTTPResponse)rv; 
    }

    public void createHandlers(Class<?> mainCLass) throws Exception {
        this.addCustom404();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(mainCLass.getPackageName()))
            .setScanners(new SubTypesScanner(), new MethodAnnotationsScanner(), new TypeAnnotationsScanner())
        );
        
        Set<Class<?>> setClazzes = reflections.getTypesAnnotatedWith(Endpoint.class);
        Set<Method> setMethods = reflections.getMethodsAnnotatedWith(Handler.class);

        for (Class<?> clazz : setClazzes){
            HttpBeanStore.addHttpBean(new Bean<>(clazz), clazz);
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

    private void addCustom404()throws Exception{
        Method m = HTTPHandlers.class.getMethod("handler404", HTTPSession.class, HTTPRequest.class);
        HttpBeanStore.addHttpBean(new Bean<>(HTTPHandlers.class), HTTPHandlers.class);
        HttpBeanStore.addBeanMethod(HTTPHandlers.class, "404", m);
    }

    public HTTPResponse handler404(HTTPSession session, HTTPRequest request) throws InterruptedException{
        return new HTTPResponse()
            .setContent("nothing here... =(".getBytes())
            .setResponseCode(404);
    }

}