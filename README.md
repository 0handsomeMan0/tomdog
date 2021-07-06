# tomdog 
基于kafka的rpc框架  
Remote Procedure Call Framework Based On Kafka

# checkout源码打成jar包

# 调用方：


## 1.

![image](https://user-images.githubusercontent.com/52146632/124593488-4a1d7200-de91-11eb-9e2e-583a23a3de2e.png)

引入jar包，properties文件配置kafka服务端地址

## 2.

![image](https://user-images.githubusercontent.com/52146632/124593698-84870f00-de91-11eb-973f-8afdf3db61d8.png)

编写类继承Command

## 3.

![image](https://user-images.githubusercontent.com/52146632/124593773-a1234700-de91-11eb-87f5-64283031aa74.png)

调用MessageSend.sendCommand方法



# 被调用方：


## 1.

![image](https://user-images.githubusercontent.com/52146632/124594048-eba4c380-de91-11eb-88d8-11d708e1c941.png)

启动类调用MessageProcessStarter.start();开启消费线程

## 2.

![image](https://user-images.githubusercontent.com/52146632/124594174-14c55400-de92-11eb-9f88-2fee80a55388.png)

被调用方法上添加 @OnCommand注解，value值为调用方Command类子类名
