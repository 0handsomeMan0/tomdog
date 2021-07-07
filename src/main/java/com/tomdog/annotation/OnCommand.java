package com.tomdog.annotation;

import java.lang.annotation.*;

/**
 * @author zhouyu
 * @description command注解
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnCommand {
    String value() default "";
}
