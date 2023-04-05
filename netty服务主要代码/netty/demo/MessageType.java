package com.lmf.blog.netty.demo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {
    PRIVATE(1),

    GROUP(2),

    ERROR(-1);

    private Integer code;

    public static MessageType match(Integer code){
        for (MessageType value : MessageType.values()) {
            if (value.code.equals(code))
                return value;
        }
        return ERROR;
    }
}
