package com.zhuyefeng.jhs.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse HTTP Request
 * 
 * @author robot
 */
public class HttpParser {
	/**
	 * 解析客户机发过的HTTP请求，如果是符合HTTP协议内容的， 返回解析结果；否则抛出IOException。 
	 * 
	 * 请求：<br>
	 * ＜request-line＞ 
	 * ＜headers＞ 
	 * ＜blank line＞
	 *  
	 * ＜request-body＞ 
	 * 
	 * 响应： 
	 * ＜status-line＞
	 * ＜headers＞ 
	 * ＜blank line＞ 
	 * 
	 * ＜response-body＞
	 */
	public static HttpRequest parse(InputStream input) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		// 分析客户请求的HTTP信息，分析出到底想访问哪个文件，
		// 发过来的HTTP请求应该是三部分。
		String inputContent = in.readLine();
		if (inputContent == null || inputContent.length() == 0) {
			throw new IOException("Client invoke error");
		}
		String request[] = inputContent.split(" ");
		if (request.length != 3) {
			throw new IOException("Client invoke error");
		}
		HttpRequest httpRequest = new HttpRequest();
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
		System.out
				.println("--------------------------------------------------");
		System.out.println("Header");
		System.out
				.println("--------------------------------------------------");
		StringBuilder builder = new StringBuilder();
		Header header = null;
		List<Header> headers = new ArrayList<Header>();
		boolean flag = true;
		while (flag) {
			inputContent = in.readLine().trim();
			// Header解析结束
			if (inputContent == null || "".equals(inputContent)) {
				flag = false;
				break;
			}
			String headerValue[] = inputContent.split(":");
			if (headerValue != null && headerValue.length == 2) {
				builder.append(headerValue[0] + " : " + headerValue[1] + "\n");
				if ("User-Agent".equals(headerValue[0])) {
					httpRequest.setUserAgent(headerValue[1]);
				} else if ("Accept-Encoding".equals(headerValue[0])) {
					httpRequest.setAcceptEncoding(headerValue[1]);
				} else if ("Accept-Language".equals(headerValue[0])) {
					httpRequest.setAcceptLanguage(headerValue[1]);
				} else if ("Accept-Charset".equals(headerValue[0])) {
					httpRequest.setAcceptCharset(headerValue[1]);
				} else {
					header = new Header(headerValue[0], headerValue[1]);
					headers.add(header);
				}
			}
		}
		httpRequest.setHeaders(headers);
		if (builder.length() > 0) {
			System.out.println(builder.toString());
		} else {
			System.out.println("Empty.");
		}
		// 解析Body
		// StringBuilder body = new StringBuilder();
		// char[] buffer = new char[1024];
		// int readLength = -1;
		// while ((readLength = in.read(buffer)) != -1) {
		// body.append(buffer, 0, readLength);
		// }
		// System.out.println(body.toString());
		return httpRequest;
	}
}
