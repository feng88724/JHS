package com.zhuyefeng.jhs.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zhuyefeng.jhs.Processor;
import com.zhuyefeng.jhs.WebConfig;

/**
 * Java NIO
 * 支持阻塞模式BIO、非阻塞模式NIO
 * 
 * SocketChannel(支持阻塞模式BIO、非阻塞模式NIO)
 * @see http://www.importnew.com/18977.html
 * 
 * ServerSocketChannel(支持阻塞模式BIO、非阻塞模式NIO)
 * @see http://www.importnew.com/18988.html
 * 
 * FileChannel(只支持阻塞模式BIO)
 * @see http://www.importnew.com/18955.html
 * @author robot
 *
 */
public class WebServer {
	ExecutorService threadPool  = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	ServerSocketChannel serverSocketChannel;
	
    public void startServer(int port) {
        try {
        	serverSocketChannel = ServerSocketChannel.open();
        	serverSocketChannel.socket().bind(new InetSocketAddress(port));
        	// 启用SO_REUSEPORT，允许绑定至同一端口
        	// serverSocketChannel.socket().setReuseAddress(true);
        	// 设置为非阻塞模型，此时accept()方法会立刻返回，返回结果可能为null
        	// serverSocketChannel.configureBlocking(false);
            System.out.println("Web Server startup on  " + port);
            SocketChannel socketChannel = null;
            while (true) {
            	socketChannel = serverSocketChannel.accept();
                // 通过多线程的方式来处理客户的请求
            	if(socketChannel != null) {
            		threadPool.execute(new Processor(socketChannel.socket()));
//            		new Thread(new Processor(socketChannel.socket())).start();
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
