package io.lightflame.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.lightflame.http.HTTPMethod;

/**
 * WebPath
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Handler {

    String value();
    HTTPMethod method() default HTTPMethod.GET; 
    
}