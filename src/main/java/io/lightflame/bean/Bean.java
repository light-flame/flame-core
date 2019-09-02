package io.lightflame.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Beans
 */
public class Bean<E>{

    private UUID key;
    private Object instance;
    private Class<?> clazz;
    private Map<String, Method> methodMap = new HashMap<>();

    public Bean(Class<?> clazz) throws Exception {
        Constructor<?> ctor = clazz.getConstructor();
        this.instance = ctor.newInstance(new Object[] { });
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
    public Object getInstance() {
        return instance;
    }


    void addMethod(String key, Method m){
        methodMap.put(key, m);
    }

    public Method getMethod(String key) {
        return methodMap.get(key);
    }

    UUID getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    void setKey(UUID key) {
        this.key = key;
    }
}