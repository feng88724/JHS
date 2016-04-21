package com.zhuyefeng.jhs.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.zhuyefeng.jhs.Processor;
import com.zhuyefeng.jhs.WebConfig;

/**
 * NIO
 * @see http://www.importnew.com/18988.html
 * @author robot
 *
 */
public class WebServer {
	ServerSocketChannel serverSocketChannel;
	
    public void startServer(int port) {
        try {
        	serverSocketChannel = ServerSocketChannel.open();
        	serverSocketChannel.socket().bind(new InetSocketAddress(port));
        	// 设置为非阻塞模型，此时accept()方法会立刻返回，返回结果可能为null
        	// serverSocketChannel.configureBlocking(false);
            System.out.println("Web Server startup on  " + port);
            SocketChannel socketChannel = null;
            while (true) {
            	System.out.println("Server Waiting...");
            	socketChannel = serverSocketChannel.accept();
                // 通过多线程的方式来处理客户的请求
            	if(socketChannel != null) {
            		new Processor(socketChannel.socket()).start();
            	}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args != null && args.length > 0 &&"-h".equals(args[0])) {
    		System.out.println("Options:");
    		System.out.println("\t-p\t指定端口");
    		System.out.println("\t-d\t指定目录");
    		System.out.println("\t-h\t查看帮助");
    	}
        new WebServer().startServer(WebConfig.HTTP_PORT);
	}

}
