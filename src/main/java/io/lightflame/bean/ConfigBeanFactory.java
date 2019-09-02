package io.lightflame.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import io.lightflame.configuration.Config;
import io.lightflame.configuration.Configuration;

/**
 * ConfigBeanFactory
 */
public class ConfigBeanFactory {

    static public void create(Class<?> mainCLass) throws Exception {
        // aqui tem q virar uma factory mesmo
        Reflections reflections = new Reflections(
                new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(mainCLass.getPackageName())).setScanners(
                        new SubTypesScanner(), new MethodAnnotationsScanner(), new TypeAnnotationsScanner()));
        Set<Class<?>> setClazzes = reflections.getTypesAnnotatedWith(Configuration.class);

        for (Class<?> clazz : setClazzes) {
            Constructor<?> ctor = clazz.getConstructor();
            Object obj = ctor.newInstance(new Object[] {});
            for (Method method : clazz.getMethods()) {
                method.invoke(obj, new Config());
                break;
                // temporary!!
            }

        }
    }
}