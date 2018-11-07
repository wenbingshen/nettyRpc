package com.wenbing.nettyrpc.server;

import com.wenbing.nettyrpc.common.RpcRequest;
import com.wenbing.nettyrpc.common.RpcResponse;
import com.wenbing.nettyrpc.registry.ServiceRegistry;
import com.wenbing.nettyrpc.utils.RpcDecoder;
import com.wenbing.nettyrpc.utils.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * RPC Server
 *
 * @author 沈文兵
 */

public class RpcServer implements ApplicationContextAware ,InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private String serverAddress;
    private ServiceRegistry serviceRegistry;

    private Map<String, Object> handlerMap = new HashMap<>();

    private NioEventLoopGroup bossGroup = null;
    private NioEventLoopGroup workerGroup = null;

    private static ThreadPoolExecutor threadPoolExecutor;

    public RpcServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcServer(String serverAddress, ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(RpcService.class).value().getName();
                logger.info("Loading service...注解上的value值为:{}"+ interfaceName);
                handlerMap.put(interfaceName, serviceBean);
                System.out.println("RpcServer的handlerMap"+handlerMap.toString());
            }
        }
    }

    //初始化bean的时候执行该方法
    @Override
    public void afterPropertiesSet() throws Exception {
        begin();
    }

    public void begin() throws Exception {
        if (bossGroup == null && workerGroup == null) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                                    .addLast(new RpcDecoder(RpcRequest.class))//将RPC请求进行解码(为了处理请求)
                                    .addLast(new RpcEncoder(RpcResponse.class))//将RPC响应进行编码(为了返回响应)
                                    .addLast(new RpcHandler(handlerMap));//处理Rpc请求
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.info("服务启动于端口:{}", port);
            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress);
            }
            //服务器同步连接断开时,这句代码才会往下执行
            future.channel().closeFuture().sync();
        }
    }

    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (RpcServer.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L,
                            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(65536));
                }
            }
        }
        threadPoolExecutor.submit(task);
    }

    public RpcServer addService(String interfaceName, Object serviceBean) {
        if (!handlerMap.containsKey(interfaceName)) {
            logger.info("Loading service: {}", interfaceName);
            handlerMap.put(interfaceName, serviceBean);
            System.out.println("addService"+handlerMap.toString());
        }

        return this;
    }
}
