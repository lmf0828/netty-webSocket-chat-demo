package com.lmf.blog.netty.demo.handler;

import com.alibaba.fastjson.JSON;
import com.lmf.blog.netty.demo.Command;
import com.lmf.blog.netty.demo.NettyServer;
import com.lmf.blog.netty.demo.Result;
import io.netty.channel.ChannelHandlerContext;

public class ConnectionHandler {
    public static void execute(ChannelHandlerContext ctx, Command command) {
        if (NettyServer.USER.containsKey(command.getNickName())) {

            ctx.channel().writeAndFlush(Result.success("您登录过了"));
            ctx.channel().disconnect();

        } else {

            NettyServer.USER.put(command.getNickName(), ctx.channel());

            ctx.channel().writeAndFlush(Result.success("加入成功"));
            ctx.channel().writeAndFlush(Result.success(command.getNickName(),null,JSON.toJSONString(NettyServer.USER.keySet())));
        }


    }
}
