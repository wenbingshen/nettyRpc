package com.wenbing.nettyrpc.client;

import com.wenbing.nettyrpc.common.RpcRequest;
import com.wenbing.nettyrpc.common.RpcResponse;
import com.wenbing.nettyrpc.utils.RpcDecoder;
import com.wenbing.nettyrpc.utils.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author 沈文兵
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        System.out.println("rpc客户端初始化......");
        cp.addLast(new RpcEncoder(RpcRequest.class));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new RpcDecoder(RpcResponse.class));
        cp.addLast(new RpcClientHandler());
    }
}
