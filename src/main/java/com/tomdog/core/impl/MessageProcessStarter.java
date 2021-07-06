package com.tomdog.core.impl;

import com.tomdog.core.AnalyzeAnnotation;

/**
 * @author zhouyu
 * @description 消息处理线程启动类
 **/
public class MessageProcessStarter{
    public static void start(){
        try {
            AnalyzeAnnotation.getAllCommandMethod();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        MessageProcess messageProcess = new MessageProcess();
        messageProcess.start();
    }
}
