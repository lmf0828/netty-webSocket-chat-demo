package com.lmf.blog.netty.demo;

import com.lmf.blog.netty.demo.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NettyServer {

    public static final Map<String, Channel> USER = new ConcurrentHashMap<>(1024);

    public static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void start() throws InterruptedException {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        new ServerBootstrap()
                .group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {

                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new HttpServerCodec())
                                //对大数据量的支持
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(1024*64))
                                //websocket   ws://localhost:8082/msg
                                .addLast(new WebSocketServerProtocolHandler("/msg"))
                                .addLast(new WebSocketHandler()); //自定义handler
                    }
                })
                .bind(8082).sync();

    }
}
