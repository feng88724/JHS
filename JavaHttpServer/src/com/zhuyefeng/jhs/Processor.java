package com.zhuyefeng.jhs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.zhuyefeng.jhs.http.Header;
import com.zhuyefeng.jhs.http.HttpParser;
import com.zhuyefeng.jhs.http.HttpRequest;

/**
 * 处理一个HTTP用户请求的线程类。
 * 
 * @author robot
 */
public class Processor implements Runnable {
	private PrintStream out;
	private InputStream input;

	public Processor(Socket socket) {
		try {
			input = socket.getInputStream();
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			HttpRequest request = HttpParser.parse(input);
			readFile(request.getRequestURI());
		} catch (IOException e) {
			e.printStackTrace();
			sendError(400, "Client invoke error");
		}
	}

	/**
	 * 处理调用一个文件的请求
	 */
	public void readFile(String fileName) throws IOException {
		// 默认首页文件名
		if(fileName == null || fileName.equals("/") || fileName.length() == 0) {
			fileName = "index.html";
		}
		File file = new File(WebConfig.WEB_ROOT + File.separator + fileName);
		if (!file.exists()) {
			sendError(404, "File Not Found");
			return;
		}
		// 把文件的内容读取到in对象中。
		InputStream in = new FileInputStream(file);
		byte content[] = new byte[(int) file.length()];
		in.read(content);
		out.println("HTTP/1.0 200 OK");
		out.println("Content-length: " + content.length);
		out.println("Server: RobotHttpServer/0.1");
		out.println();
		out.write(content);
		out.flush();
		out.close();
		in.close();
	}

	/**
	 * 发送错误的信息
	 */
	public void sendError(int errCode, String errMsg) {
		out.println("HTTP/1.0 " + errCode + " " + errMsg);
		out.println("Content-type: text/html");
		out.println("Server: RobotHttpServer/0.1");
		out.println();
		out.println("<html>");
		out.println("<head><title>Error " + errCode + "--" + errMsg
				+ "</title></head>");
		out.println("<h1>" + errCode + " " + errMsg + "</h1>");
		out.println("</html>");
		out.println();
		out.flush();
		out.close();
	}
}
