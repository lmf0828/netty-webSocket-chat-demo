package com.lmf.blog.netty.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage extends  Command{

    private Integer type;  //消息类型
    private String content; //内容
    private String target; //接收人
    private String from; //发送人
}
