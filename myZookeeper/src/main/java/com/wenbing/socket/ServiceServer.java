package com.wenbing.socket;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceServer {

    public static void main(String[] args) throws Exception{
//        创建一个serversocket,绑定到本机的8899端口上
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("localhost", 8899));

//        接收客户端的连接；accept是一个阻塞方法，会一直等待，到有客户端请求连接才返回
        while (true) {
            Socket socket = server.accept();
//            每个socket请求都给一个线程来处理
            new Thread(new ServiceServerTask(socket)).start();

        }

    }

}
