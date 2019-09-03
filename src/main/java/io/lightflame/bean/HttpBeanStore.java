package io.lightflame.bean;

import java.util.HashMap;
import java.util.Map;

import io.lightflame.functions.HttpFunction;


/**
 * HttpBeanStore
 */
public class HttpBeanStore {

    static public HttpFunction getFunction(String url){
        return HttpFunctionStore.getFunctionByUrl(url);
    }

    public HttpFunctionStore addHttpBean(Object e){
        Bean<?> bean = new Bean<>(e);
        return new HttpFunctionStore(bean);
    }

    static public class HttpFunctionStore{

        static private Map<String, Bean<?>> beanMap = new HashMap<>();
        private Bean<?> bean;

        HttpFunctionStore(Bean<?> bean) {
            this.bean = bean;
        }

        /**
         * @return the beanMap
         */
        static HttpFunction getFunctionByUrl(String url) {
            Bean<?> bean =  beanMap.get(url);
            if (bean == null){
                return null;
            }
            return (HttpFunction)bean.getFunction();
        }

        public HttpFunctionStore httpGET(String url,HttpFunction func){
            bean.setFunction(func);
            beanMap.put(url, bean);
            return this;
        }

    }

    // static public void addBeanMethod(Class<?> clazz, String url, Method method){
    //     Bean<?> bean = getBean(clazz);
    //     bean.addMethod(url, method);
    //     mapUrlClazz.put(url, clazz);
    // }

    
}