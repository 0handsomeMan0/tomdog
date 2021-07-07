package com.tomdog.entity;

import java.io.Serializable;

/**
 * @author zhouyu
 * @description 命令实体类,待传递参数需继承该类
 **/
public class Command<T> implements Serializable {
    private static final long serialVersionUID = 199886L;
    /**
     * 待传递参数
     */
    private  T param;

    public Command(T param) {
        this.param = param;
    }

    public Command() {
    }


    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
