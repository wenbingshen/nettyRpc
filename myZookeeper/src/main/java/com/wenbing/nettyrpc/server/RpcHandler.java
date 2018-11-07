package com.wenbing.nettyrpc.server;


import com.wenbing.nettyrpc.common.RpcRequest;
import com.wenbing.nettyrpc.common.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * RPC handler
 * 对客户端发送过来的request的处理器
 *
 * @author 沈文兵
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
        RpcServer.submit(new Runnable() {
            @Override
            public void run() {
                logger.info(request.toString()+"哈哈哈");
                logger.info("Receive request -->"+request.getRequestId());
                RpcResponse response = new RpcResponse();
                response.setRequestId(request.getRequestId());
                try {
                    Object result = handle(request);
                    System.out.println("处理的结果为:"+result);
                    response.setResult(result);
                } catch (Throwable t) {
                    response.setError(t.toString());
                    logger.error("RPC服务端处理器处理request出错",t);
                }
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        logger.info("send response for request :"+request.getRequestId());
                    }
                });
            }
        });
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceBeanClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        logger.debug(serviceBeanClass.getName());
        logger.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            logger.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            logger.debug(parameters[i].toString());
        }
        //反射
        Method method = serviceBeanClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server caught exception", cause);
        ctx.close();
    }
}
