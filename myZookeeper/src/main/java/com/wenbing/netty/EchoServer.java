package com.wenbing.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

//服务端模板
public class EchoServer {
    private final String host;
    private final int port;

    public EchoServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup eventLoopGroup = null;
        try {
            //server端引导类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //连接池处理数据
            eventLoopGroup = new NioEventLoopGroup();
            //装配bootstrap
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class) //指定通道类型为NioServerSocketChannel,一种异步模式
                    .localAddress(host,port)//设置InetSocketAddress,让服务器监听本地port端口等待客户端连接
                    .childHandler(new ChannelInitializer<Channel>() {//设置childHandler处理所有连接请求
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());//注册EchoServerHandler处理器
                        }
                    });
            //最后绑定服务器等待绑定完成，调用sync()方法会阻塞直到服务器完成绑定,然后服务器等待通道关闭，因为使用sync()
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("开始监听，端口为：" + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new EchoServer("localhost",20000).start();
    }


}
