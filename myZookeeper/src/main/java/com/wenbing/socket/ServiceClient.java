package com.wenbing.socket;

import java.io.*;
import java.net.Socket;

public class ServiceClient {

    public static void main(String[] args) throws Exception {
//        向服务器发出请求建立连接
        Socket socket = new Socket("localhost", 8899);
//        从socket中获取输出流
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        PrintWriter printWriter = new PrintWriter(outputStream);
        printWriter.println("hello");
        printWriter.flush();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String result = bufferedReader.readLine();
        System.out.println(result);

        inputStream.close();
        outputStream.close();
        socket.close();
    }

}
