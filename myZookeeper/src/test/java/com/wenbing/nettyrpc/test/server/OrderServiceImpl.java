package com.wenbing.nettyrpc.test.server;


import com.wenbing.nettyrpc.test.client.OrderService;
import com.wenbing.nettyrpc.server.RpcService;

@RpcService(OrderService.class)
public class OrderServiceImpl implements OrderService{

    public OrderServiceImpl() {
    }

    @Override
    public int getPrice(int size) {
        System.out.println("进入了OrderServiceImpl的getPrice()方法，结果为"+50*size);
        return 50*size;
    }
}
