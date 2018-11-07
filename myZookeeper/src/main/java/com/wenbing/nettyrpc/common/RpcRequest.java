package com.wenbing.nettyrpc.common;

import lombok.Data;

import java.util.Arrays;

/**
 * Rpc Request 发送请求的对象
 *
 * @author 沈文兵
 */
@Data
public class RpcRequest {

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}
