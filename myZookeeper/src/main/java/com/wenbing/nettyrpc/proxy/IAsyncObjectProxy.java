package com.wenbing.nettyrpc.proxy;


import com.wenbing.nettyrpc.client.RPCFuture;

/**
 * @author 沈文兵
 */
public interface IAsyncObjectProxy {
    public RPCFuture call(String funcName, Object... args);
}