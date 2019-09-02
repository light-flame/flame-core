package io.lightflame.bean;

/**
 * ConfigBeanStore
 */
public class ConfigBeanStore {

    @SuppressWarnings("unchecked")
    public <E>E getBean(Class<E> clazz){
        return (E)BeanStore.getBean(clazz).getInstance();
    }
    
}