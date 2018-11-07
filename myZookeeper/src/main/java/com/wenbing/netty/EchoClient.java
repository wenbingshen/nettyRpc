package com.wenbing.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

//客户端模板
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup nioEventLoopGroup = null;
        try {
         //客户端引导类
            Bootstrap bootstrap = new Bootstrap();
            //EventLoopGroup可以理解为是一个线程池,这个线程池用来处理连接、接受数据、发送数据
            nioEventLoopGroup = new NioEventLoopGroup();
            //装配bootstrap
            bootstrap.group(nioEventLoopGroup)//多线程处理
                    .channel(NioSocketChannel.class)//指定通道类型为NioSocketChannel
                    .remoteAddress(new InetSocketAddress(host, port))//服务器地址
                    .handler(new ChannelInitializer<SocketChannel>() {//业务处理类
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());//注册处理器
                        }
                    });
            //连接服务器
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            nioEventLoopGroup.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new EchoClient("localhost", 20000).start();
    }
}
