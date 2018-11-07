package com.wenbing.nio;

public class TimeServer {
    public static void main(String[] args) {
        int port = 8085;
        if (args != null && args.length < 0) {
            port = Integer.valueOf(args[0]);
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
