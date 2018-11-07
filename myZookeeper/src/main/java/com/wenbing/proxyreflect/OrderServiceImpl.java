package com.wenbing.proxyreflect;

public class OrderServiceImpl implements OrderService {
    @Override
    public int sum(int total) {
        return total*10;
    }

    @Override
    public Long getPrice(int size) {
        return size*30L;
    }
}
