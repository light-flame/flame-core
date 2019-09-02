package io.lightflame.http;

import java.lang.reflect.Method;
import io.lightflame.bean.Bean;
import io.lightflame.bean.HttpBeanStore;

/**
 * HTTPHandlers
 */
public class HTTPHandlers {

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



    public HTTPResponse handler404(HTTPSession session, HTTPRequest request) throws InterruptedException{
        return new HTTPResponse()
            .setContent("nothing here... =(".getBytes())
            .setResponseCode(404);
    }

}