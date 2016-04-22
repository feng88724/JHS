package com.zhuyefeng.jhs.http;

import java.util.List;

/**
 * HTTP请求
 * 
 * @author robot
 */
public class HttpRequest {
	// 请求方法
	private String method;

	// 协议版本
	private String protocol;
	private String requestURL;

	// 请求的URI地址 在HTTP请求的第一行的请求方法后面
	private String requestURI;

	// 请求的主机信息
	private String host;

	// 代理，用来标识代理的浏览器信息 ,对应HTTP请求中的User-Agent
	private String userAgent;

	// 对应Accept-Language
	private String acceptLanguage;

	// 请求的编码方式 对应HTTP请求中的Accept-Encoding
	private String acceptEncoding;

	// 请求的字符编码 对应HTTP请求中的Accept-Charset
	private String acceptCharset;

	private String contentType;
	private String contentLength;

	private List<Header> headers;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getAcceptEncoding() {
		return acceptEncoding;
	}

	public void setAcceptEncoding(String acceptEncoding) {
		this.acceptEncoding = acceptEncoding;
	}

	public String getAcceptCharset() {
		return acceptCharset;
	}

	public void setAcceptCharset(String acceptCharset) {
		this.acceptCharset = acceptCharset;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentLength() {
		return contentLength;
	}

	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Header> headers) {
		this.headers = headers;
	}
}
