# Netty-WebSocket-chat-demo

#### 介绍
前端vue jchat组件 + 后端springboot 实现的netty+WebSocket在线聊天demo

#### 使用说明

#### 前端

已经给出vue页面和后端主要代码

正常设置路由，成功路由到chat页面即可开始连接后端代码，连接失败会报错，不影响

#### 后端

在你新建的boot项目**启动目录**下将给出的后端目录粘贴即可，并在**主启动类**加上以下代码

```java
     try {
            NettyServer.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
```

只做了部分功能，毕竟是demo。
