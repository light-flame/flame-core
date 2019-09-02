package io.lightflame.bean;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handle
 */
public abstract class BeanStore{
    
    static private Map<UUID, Bean<?>> beanMap = new ConcurrentHashMap<>();

    // abstract public Bean<?> getBean();

    static public Bean<?> getBean(UUID key){
        if (key == null){
            return null;
        }
        return beanMap.get(key);
    }

    static public UUID addBean(Bean<?> bean){
        UUID uuid =  UUID.randomUUID();
        beanMap.put(uuid, bean);
        return uuid;
    }
    
    static public void setBean(Bean<?> bean){
        beanMap.replace(bean.getKey(), bean);
    }
}