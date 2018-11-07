package com.wenbing.nettyrpc.test;

import com.wenbing.nettyrpc.server.RpcService;

@RpcService(OrderService.class)
public class OrderServiceImpl implements OrderService {
    @Override
    public int getPrice(int size) {
        System.out.println("进入了OrderServiceImpl的getPrice()方法，结果为"+size*50);
        return size*50;
    }
}
