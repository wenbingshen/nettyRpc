package com.wenbing.socket;

import java.io.*;
import java.net.Socket;

public class ServiceServerTask implements Runnable {

    Socket socket;
    InputStream inputStream = null;
    OutputStream outputStream = null;

    public ServiceServerTask(Socket socket) {
        this.socket = socket;
    }

    //    业务逻辑，跟客户端进行数据交互
    @Override
    public void run() {

        try {
//            从socket连接中获取到与client之间的网络通信输入流
            inputStream = socket.getInputStream();
//            从socket连接中获取到与client之间的网络通信输出流
            outputStream = socket.getOutputStream();

//            BufferedReader读取数据是一个字符一个字符读取，遇到回车即停止
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//            从网络通信输入流中读取客户端发送过来的数据，读取一行，遇到回车即结束
            // 注意:socketinputstream的读数据的方法是阻塞的
            //读取很多行数据，用while循环读
//            String line = "";
//            String param = "";
//            while (( line = br.readLine()) != null) {
//                param = br.readLine();
//                System.out.println(param);
//            }
            String param = br.readLine();


            /**
             * 作业:
             * 将以下业务调用逻辑写成更加通用的，可以根据客户端发过来的调用类名、调用方法名、调用参数来灵活调用
             *
             * 运用反射技术、动态代理(增强方法)
             *
             */


            System.out.println(param);
            GetDataServiceImpl getDataService = new GetDataServiceImpl();
            String result = getDataService.getData(param);

//            将调用结果写到socket的输出流中，以发送给客户端
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
//                关流，现在是在每个线程里
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
