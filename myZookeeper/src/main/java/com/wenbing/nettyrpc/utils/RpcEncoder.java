package com.wenbing.nettyrpc.utils;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Rpc Encoder
 *
 * @author 沈文兵
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        if (genericClass.isInstance(o)) {
            byte[] data = SerializationUtil.serialize(o);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
