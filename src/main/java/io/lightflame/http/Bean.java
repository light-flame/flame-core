package io.lightflame.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Beans
 */
public class Bean {

    private Object instance;
    private Method m;

    public Bean(Method m, Class<?> clazz) throws Exception {
        Constructor<?> ctor = clazz.getConstructor();
        this.instance = ctor.newInstance(new Object[] { });
        this.m = m;
    }

    public HTTPResponse getResponse(HTTPSession session, HTTPRequest request) throws Exception {
        Object rv = m.invoke(instance, session, request);  
        return (HTTPResponse)rv; 
    }
}