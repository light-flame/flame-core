package io.lightflame.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handle
 */
public abstract class BeanStore{
    
    static private Map<String, Bean<?>> beanMap = new ConcurrentHashMap<>();

    // abstract public Bean<?> getBean();

    static public Bean<?> getBean(Class<?> key){
        if (key == null){
            return null;
        }
        return beanMap.get(key.getName());
    }

    static public void addBean(Bean<?> bean){
        beanMap.put(bean.getClazz().getName(), bean);
    }
    
    static public void setBean(Bean<?> bean){
        beanMap.replace(bean.getClazz().getName(), bean);
    }
}