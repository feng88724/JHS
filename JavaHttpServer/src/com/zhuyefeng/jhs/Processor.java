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
import com.zhuyefeng.jhs.http.HttpRequest;

/**
 * 处理一个HTTP用户请求的线程类。
 * 
 * @author robot
 */
public class Processor implements Runnable {
	private PrintStream out;
	private InputStream input;
	private HttpRequest httpRequest;

	public Processor(Socket socket) {
		try {
			httpRequest =  new HttpRequest();
			input = socket.getInputStream();
			out = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String fileName = parse(input);
			readFile(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析客户机发过的所有HTTP请求，如果是符合HTTP协议内容的， 就分析出客户机所要访问文件的名字，并且返回文件名。
	 */
	public String parse(InputStream input) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		String inputContent = in.readLine();
		if (inputContent == null || inputContent.length() == 0) {
			sendError(400, "Client invoke error");
			return null;
		}
		// 分析客户请求的HTTP信息，分析出到底想访问哪个文件，
		// 发过来的HTTP请求应该是三部分。
		String request[] = inputContent.split(" ");
		if (request.length != 3) {
			sendError(400, "Client invoke error");
			return null;
		}
		// 第一部分是请求的方法
		String method = request[0];
		// 第二部分是请求的文件名
		String fileName = request[1];
		// 第三部分是HTTP版本号
		String httpVersion = request[2];
		httpRequest.setMethod(method);
		httpRequest.setRequestURI(fileName);
		httpRequest.setProtocol(httpVersion);
		System.out.println("# " + Thread.currentThread().getId() + ", Method: " + method + ", file name: " + fileName + ", HTTP version: " + httpVersion);

		// 解析Header
		System.out.println("--------------------------------------------------");
		System.out.println("Header");
		System.out.println("--------------------------------------------------");
		StringBuilder builder = new StringBuilder();
		Header header = null;
		List<Header> headers = new ArrayList<Header>();
		boolean flag = true;
		while(flag) {
			inputContent = in.readLine().trim();
			// Header解析结束
			if(inputContent == null || "".equals(inputContent)) {
				flag = false;
				break;
			}
			String headerValue[] = inputContent.split(":");
			if(headerValue != null && headerValue.length == 2){
				builder.append(headerValue[0] + " : " + headerValue[1] + "\n");
				if("User-Agent".equals(headerValue[0])) {
					httpRequest.setUserAgent(headerValue[1]);
				} else if("Accept-Encoding".equals(headerValue[0])) {
					httpRequest.setAcceptEncoding(headerValue[1]);
				} else if("Accept-Language".equals(headerValue[0])) {
					httpRequest.setAcceptLanguage(headerValue[1]);
				} else if("Accept-Charset".equals(headerValue[0])) {
					httpRequest.setAcceptCharset(headerValue[1]);
				} else {
					header = new Header(headerValue[0], headerValue[1]);
					headers.add(header);
				}
			}
		}
		httpRequest.setHeaders(headers);
		if(builder.length() > 0) {
			System.out.println(builder.toString());
		} else {
			System.out.println("Empty.");
		}
		// 解析Body
//		StringBuilder body = new StringBuilder();
//		char[] buffer = new char[1024];
//		int readLength = -1;
//		while ((readLength = in.read(buffer)) != -1) {
//			body.append(buffer, 0, readLength);
//		}
//		System.out.println(body.toString());
		return fileName;
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
