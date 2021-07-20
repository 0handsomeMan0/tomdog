package com.tomdog.core;

import com.tomdog.annotation.OnCommand;
import com.tomdog.core.MetadataCollection;
import com.tomdog.entity.MethodBean;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author zhouyu
 * @description 获取所有OnCommand注解的方法
 **/
public class AnalyzeAnnotation {
    public static void getAllCommandMethod() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(Collections.singletonList(new MethodAnnotationsScanner()));
        Set<Method> classes = reflections.getMethodsAnnotatedWith(OnCommand.class);
        for (Method clazz : classes) {
            String topic = clazz.getAnnotation(OnCommand.class).value()+"_"+UUID.randomUUID();
            Object instance = clazz.getDeclaringClass().newInstance();
            Class<?>[] parameterTypes = clazz.getParameterTypes();
            MethodBean methodBean = MethodBean.MethodBeanBuilder.builder().withObject(instance).withMethod(clazz).build();
            MetadataCollection.allCommandBean.put(topic,methodBean);
        }
    }

}
