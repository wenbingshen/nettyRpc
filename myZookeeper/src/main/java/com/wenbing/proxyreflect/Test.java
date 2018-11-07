package com.wenbing.proxyreflect;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) throws Exception {
        OrderServiceImpl orderService = new OrderServiceImpl();

//        获取代理
        OrderService os = (OrderService) Proxy.newProxyInstance(Test.class.getClassLoader(), orderService.getClass().getInterfaces(), new com.wenbing.proxyreflect.Proxy(orderService));
        Long price = os.getPrice(10);
        System.out.println("价格为："+price);
        System.out.println("=========================");
        int sum = os.sum(5);
        System.out.println(sum);
    }

}
