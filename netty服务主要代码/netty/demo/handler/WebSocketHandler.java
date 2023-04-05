package com.lmf.blog.netty.demo.handler;

import com.alibaba.fastjson.JSON;
import com.lmf.blog.netty.demo.Command;
import com.lmf.blog.netty.demo.CommandType;
import com.lmf.blog.netty.demo.NettyServer;
import com.lmf.blog.netty.demo.Result;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {

        try {
            Command command = JSON.parseObject(frame.text(), Command.class);

            switch (CommandType.match(command.getCode())){
                case CONNECT:ConnectionHandler.execute(ctx,command);break;
                case CHAT:ChatHandler.execute(ctx,frame);break;
                case JOIN_GROUP:{
                    NettyServer.USER.put(command.getNickName(), ctx.channel());
                    NettyServer.GROUP.add(ctx.channel());
                    NettyServer.GROUP.writeAndFlush(Result.success(command.getNickName(),null,JSON.toJSONString(NettyServer.USER.keySet())));
                };break;
                default:ctx.channel().writeAndFlush(Result.fail("不支持的类型"));
            }
        } catch (Exception e) {
            ctx.channel().writeAndFlush(Result.fail(e.getMessage()));
        }
    }
}

