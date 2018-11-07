package com.wenbing.nettyrpc.test.app;


import com.wenbing.nettyrpc.client.RPCFuture;
import com.wenbing.nettyrpc.proxy.IAsyncObjectProxy;
import com.wenbing.nettyrpc.test.client.HelloService;
import com.wenbing.nettyrpc.test.client.OrderService;
import com.wenbing.nettyrpc.client.RpcClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client-spring.xml")
public class ClientTest {

    @Autowired
    private RpcClient rpcClient;

    @Test
    public void getPrice() {
        OrderService orderService = rpcClient.create(OrderService.class);
        IAsyncObjectProxy async = rpcClient.createAsync(HelloService.class);
        RPCFuture rpcFuture = async.call("hello", "小红");
        try {
            Object o = rpcFuture.get();
            System.out.println(o+"kkk");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        int price = orderService.getPrice(20);
        System.out.println("最后结果这里打印了----->" + price);
    }


}
