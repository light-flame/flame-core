package io.lightflame.bean;


import io.lightflame.functions.BeanFunction;

/**
 * Beans
 */
public class Bean<E>{

    private E instance;
    private BeanFunction function;

    public Bean(E instance) {
        this.instance = instance;
    }

    public E getInstance() {
        return instance;
    }

    /**
     * @param function the function to set
     */
    public void setFunction(BeanFunction function) {
        this.function = function;
    }

    /**
     * @return the function
     */
    public BeanFunction getFunction() {
        return function;
    }

}