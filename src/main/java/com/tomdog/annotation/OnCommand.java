package com.tomdog.annotation;

import java.lang.annotation.*;

/**
 * @author zhouyu
 * @description 接受消息
 **/
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnCommand {
    String value() default "";
}
