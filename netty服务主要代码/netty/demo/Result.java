package com.lmf.blog.netty.demo;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class Result {
    private String name;
    private LocalDateTime time;
    private String from;
    private String msg;

    public static TextWebSocketFrame fail(String message){
        return new TextWebSocketFrame(JSON.toJSONString(new Result("系统消息",LocalDateTime.now(),null,message)));
    }

    public static TextWebSocketFrame success(String message){
        return new TextWebSocketFrame(JSON.toJSONString(new Result("系统消息",LocalDateTime.now(),null,message)));
    }

    public static TextWebSocketFrame success(String user,String from,String message){
        return new TextWebSocketFrame(JSON.toJSONString(new Result(user,LocalDateTime.now(),from,message)));
    }
}
