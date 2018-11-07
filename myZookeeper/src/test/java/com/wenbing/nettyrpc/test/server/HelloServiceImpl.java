package com.wenbing.nettyrpc.test.server;

import com.wenbing.nettyrpc.test.client.HelloService;
import com.wenbing.nettyrpc.test.client.Person;
import com.wenbing.nettyrpc.server.RpcService;

@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {

    public HelloServiceImpl(){

    }

    @Override
    public String hello(String name) {
        return "Hello! " + name;
    }

    @Override
    public String hello(Person person) {
        return "Hello! " + person.getFirstName() + " " + person.getLastName();
    }
}
