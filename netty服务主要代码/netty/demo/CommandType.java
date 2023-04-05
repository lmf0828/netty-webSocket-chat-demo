package com.lmf.blog.netty.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandType {

    CONNECT(10001),  //建立连接

    ERROR(-1),   //错误

    JOIN_GROUP(10003),//加入群

    CHAT(10002);  //消息

    private Integer code;

    public static CommandType match(Integer code){
        for (CommandType value : CommandType.values()) {
            if (value.getCode().equals(code)){
                return value;
            }
        }
        return ERROR;
    }
}
