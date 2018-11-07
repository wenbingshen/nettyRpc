package com.wenbing.nettyrpc.common;

import lombok.Data;

/**
 * Rpc Response 服务端返回的结果对象
 *
 * @author 沈文兵
 */
@Data
public class RpcResponse {

    private String requestId;
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }


}
