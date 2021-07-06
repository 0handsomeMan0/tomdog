package com.tomdog.entity;

import java.lang.reflect.Method;

/**
 * @author zhouyu
 * @description bean集合
 **/
public class MethodBean {
    private Object object;
    private Method method;
    private MethodBean(Object object,Method method){
        this.object=object;
        this.method=method;
    }

    private void setObject(Object o){
        this.object=o;
    }
    public Object getObject(){
        return this.object;
    }
    private void setMethod(Method method){
        this.method=method;
    }
    public Method getMethod(){
        return this.method;
    }


    public static final class MethodBeanBuilder {
        private Object object;
        private Method method;

        private MethodBeanBuilder() {
        }

        public static MethodBeanBuilder builder() {
            return new MethodBeanBuilder();
        }

        public MethodBeanBuilder withObject(Object object) {
            this.object = object;
            return this;
        }

        public MethodBeanBuilder withMethod(Method method) {
            this.method = method;
            return this;
        }
        public MethodBean build() {
            return new MethodBean(this.object, this.method);
        }
    }
}
