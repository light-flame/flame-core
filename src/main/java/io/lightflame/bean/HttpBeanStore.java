package io.lightflame.bean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * HttpBeanStore
 */
public class HttpBeanStore {

    // clazz - beanID
    static private Map<Class<?>,UUID> converterMap = new HashMap<>();
    // url - clazz
    static private Map<String,Class<?>> mapUrlClazz = new HashMap<>();

    static public Bean<?> getBean(Class<?> clazz){
        return BeanStore.getBean(converterMap.get(clazz));
    }

    static public Bean<?> getBeanByURL(String url){
        Class<?> clazz =  mapUrlClazz.get(url);
        return getBean(clazz);
    }

    static public void addHttpBean(Bean<?> bean, Class<?> clazz){
        UUID uuid = BeanStore.addBean(bean);
        converterMap.put(clazz, uuid);
    }

    static public void addBeanMethod(Class<?> clazz, String url, Method method){
        Bean<?> bean = getBean(clazz);
        bean.addMethod(url, method);
        mapUrlClazz.put(url, clazz);
    }
}