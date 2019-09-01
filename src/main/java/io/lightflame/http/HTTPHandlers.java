package io.lightflame.http;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
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

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

    private static final Logger LOGGER = Logger.getLogger(HTTPHandlers.class);

    public HTTPResponse getHandle(HTTPSession session, HTTPRequest request) throws Exception{
        Bean handler = BeanStore.getBean(request.getLocation());
        if (handler == null) {
            handler = BeanStore.getBean("404");
        }
        return handler.getResponse(session, request);
    }

    public void createHandlers(Class<?> mainCLass) throws Exception {
        this.addCustom404();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
            .setUrls(ClasspathHelper.forPackage(mainCLass.getPackageName()))
            .setScanners(new SubTypesScanner(), new MethodAnnotationsScanner(), new TypeAnnotationsScanner())
        );
        
        Map<String, Class<?>> mapClazzes = getMapCLazzes(reflections.getTypesAnnotatedWith(Endpoint.class));
        Set<Method> setMethods = reflections.getMethodsAnnotatedWith(Handler.class);

        for (Method method : setMethods){
            String url = "";
            Class<?> clazz = mapClazzes.get(method.getDeclaringClass().getName());
            if (clazz != null){
                Endpoint webPath =  clazz.getAnnotation(Endpoint.class);
                url = webPath == null ? "/" : webPath.value();
            }

            Handler webPath = method.getAnnotation(Handler.class);
            url += webPath.value();
            BeanStore.addBean(url, new Bean(method, clazz));
            LOGGER.info("registering url at: " + url);
        }
    }

    private void addCustom404()throws Exception{
        Method m = HTTPHandlers.class.getMethod("handler404", HTTPSession.class, HTTPRequest.class);
        BeanStore.addBean("404", new Bean(m, HTTPHandlers.class));
    }

    public HTTPResponse handler404(HTTPSession session, HTTPRequest request) throws InterruptedException{
        return new HTTPResponse()
            .setContent("nothing here... =(".getBytes())
            .setResponseCode(404);
    }

    private static Map<String, Class<?>> getMapCLazzes(Set<Class<?>> setClazzes){
        Map<String, Class<?>> mapClazzes = new HashMap<>();
        for (Class<?> clazz : setClazzes){
            mapClazzes.put(clazz.getName(), clazz);
        }
        return mapClazzes;
    }

}