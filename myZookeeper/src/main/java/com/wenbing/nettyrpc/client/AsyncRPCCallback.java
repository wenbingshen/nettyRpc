package com.wenbing.nettyrpc.client;

/**
 * @author 沈文兵
 */
public interface AsyncRPCCallback {

    void success(Object result);

    void fail(Exception e);

}
