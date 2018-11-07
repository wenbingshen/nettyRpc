package com.wenbing.nettyrpc.server;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义RPCService注解 for RPC service
 *
 * @author 沈文兵
 */

@Target({ElementType.TYPE})//注解用在接口上
@Retention(RetentionPolicy.RUNTIME)//VM将在运行期间保存注解，因此可以通过反射机制读取注解的信息
@Component
public @interface RpcService {
    Class<?> value();
}
