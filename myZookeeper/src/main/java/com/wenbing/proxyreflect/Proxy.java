package com.wenbing.proxyreflect;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//代理类
public class Proxy implements InvocationHandler {

//    需要代理的目标
    private Object target;

//    使用构造方法传入需要代理的目标
    public Proxy(Object target) {
        this.target = target;
    }


    /**
     * 横切面逻辑
     *
     * @param proxy  产生代理的实例
     * @param method 需要代理的方法
     * @param args   方法参数
     * @return 代理方法的返回值
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

//        方法执行前的业务
        System.out.println("方法执行前的业务");

//        使用反射执行方法
        Object result = method.invoke(target, args);
        System.out.println(result);

//        方法执行后的业务
        System.out.println("方法执行后的业务");
        return result;
    }
}
