package com.lmf.blog.netty.demo.handler;

import com.alibaba.fastjson.JSON;
import com.lmf.blog.netty.demo.ChatMessage;
import com.lmf.blog.netty.demo.MessageType;
import com.lmf.blog.netty.demo.NettyServer;
import com.lmf.blog.netty.demo.Result;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class ChatHandler {
    public static void execute(ChannelHandlerContext ctx, TextWebSocketFrame frame) {

        ChatMessage chatMessage = JSON.parseObject(frame.text(), ChatMessage.class);
        switch (MessageType.match(chatMessage.getType())) {
            case PRIVATE:{
                if (chatMessage.getTarget()==null||"".equals(chatMessage.getTarget())){
                    ctx.channel().writeAndFlush(Result.fail("发送失败，请指定发送对象"));
                }
                Channel channel = NettyServer.USER.get(chatMessage.getTarget());
                //System.out.println("接收人:"+chatMessage.getTarget());
                if (!channel.isActive()){
                    ctx.channel().writeAndFlush(Result.fail("对方不在线"));
                }else{
                    channel.writeAndFlush(Result.success("private",chatMessage.getFrom(),chatMessage.getContent()));

                   // System.out.println("消息内容:"+chatMessage.getContent());
                }
            };break;
            default:ctx.channel().writeAndFlush(Result.fail("不支持的类型"));
        }

    }
}
