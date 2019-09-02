package io.lightflame.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Beans
 */
public class Bean<E>{

    private E instance;
    private Class<E> clazz;
    private Map<String, Method> methodMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public Bean(Class<E> clazz) throws Exception {
        Constructor<E> ctor = clazz.getConstructor();
        E[] ts = (E[]) new Object[0];
        this.instance = ctor.newInstance(ts);
        this.clazz = clazz;
    }

    /**
     * @return the clazz
     */
    Class<?> getClazz() {
        return clazz;
    }

    /**
     * @return the instance
     */
    public E getInstance() {
        return instance;
    }


    void addMethod(String key, Method m){
        methodMap.put(key, m);
    }

    public Method getMethod(String key) {
        return methodMap.get(key);
    }
}