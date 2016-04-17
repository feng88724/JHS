package com.zhuyefeng.jhs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private ServerSocket serverSocket;

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Web Server startup on  " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                // 通过多线程的方式来处理客户的请求
                new Processor(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * WebServer类的启动方法
     * 可以通过命令行参数指定当前Web服务器所使用的端口号、默认目录等。
     */
    public static void main(String[] argv) throws Exception {
    	if(argv != null && "-h".equals(argv[0])) {
    		System.out.println("Options:");
    		System.out.println("\t-p\t指定端口");
    		System.out.println("\t-d\t指定目录");
    		System.out.println("\t-h\t查看帮助");
    	}
        new WebServer().startServer(WebConfig.HTTP_PORT);
    }
}