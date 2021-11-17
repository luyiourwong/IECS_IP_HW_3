package com.fcu.m1007888.ip.hw3;

/**
 * HttpRequest - HTTP request container and parser
 *
 * $Id: HttpRequest.java,v 1.2 2003/11/26 18:11:53 kangasha Exp $
 *
 */

import java.io.*;

public class HttpRequest {
    /** Help variables */
    final static String CRLF = "\r\n";
    final static int HTTP_PORT = 80;
    /** Store the request parameters */
    String method;
    String URI;
    String version;
    String headers = "";
    /** Server and port */
    private String host;
    private int port;
    
    /** Maximum size of objects that this proxy can handle. For the
     * moment set to 100 KB. You can adjust this as needed. */
    final static int MAX_OBJECT_SIZE = 100000;
    /* 
     * POST 用
     * 
     */
    boolean isPOST = false;
    int bodyLen = 0;
    String postBody = "";
    public boolean isPOST() {
    	return isPOST;
    }
    
    /*
     * 拒絕連線用
     */
    private boolean ERROR = false;
    public boolean isERROR() {
    	return ERROR;
    }

    /** Create HttpRequest by reading it from the client socket */
    public HttpRequest(BufferedReader from) {
    	System.out.println("[HttpRequest] 開始建立請求");
		String firstLine = "";
		try {
		    firstLine = from.readLine();
		    System.out.println("[HttpRequest] 讀取到輸入");
		} catch (IOException e) {
		    System.out.println("Error reading request line: " + e);
		}
		
		if(firstLine == null || firstLine.isEmpty() || firstLine.isBlank()) {
			ERROR = true;
			return;
		}
		
		/* Fill in 
		 * 
		 * 內容格式為:
		 * GET go.microsoft.com:443 HTTP/1.0
		 * 
		 * */
		System.out.println("firstLine is: " + firstLine);
		String[] tmp = firstLine.split(" ");
		method = tmp[0];
		URI = tmp[1];
		version = tmp[2];
	
		System.out.println("URI is: " + URI);
	
		if (!method.equals("GET") && !method.equals("POST")) { //除了GET跟POST以外都拒絕
			System.out.println("[Request] Error: Method not GET or POST");
		    ERROR = true;
			return;
		}
		if (method.equals("POST")) { //判定是不是POST
			isPOST = true;
		}
		try {
		    String line = from.readLine();
		    while (line.length() != 0) {
				headers += line + CRLF;
				/* We need to find host header to know which server to
				 * contact in case the request URI is not complete. */
				if (line.startsWith("Host:")) {
				    tmp = line.split(" ");
				    if (tmp[1].indexOf(':') > 0) {
						String[] tmp2 = tmp[1].split(":");
						host = tmp2[0];
						port = Integer.parseInt(tmp2[1]);
					    } else {
						host = tmp[1];
						port = HTTP_PORT;
				    }
				}
				/*
				 * POST
				 * 如果抓到Content-Length:開頭，儲存body的長度
				 */
				if (line.startsWith("Content-Length") || line.startsWith("Content-length"))
                {
                    String[] tmpStr = line.split(" ");
                    bodyLen = Integer.parseInt(tmpStr[1]);
                }
				if (isPOST) { //顯示body，debug用
					System.out.println("" + line);
				}
				/*
				 * 換行
				 */
				line = from.readLine();
		    }
		    if (isPOST) {
		    	/*
		    	 * 跟response一樣，把內容寫入body
		    	 */
				char[] body = new char[bodyLen];
                from.read(body, 0, bodyLen);
                postBody = new String(body);
			}
		} catch (IOException e) {
		    System.out.println("Error reading from socket: " + e);
		    return;
		}
		System.out.println("Host to contact is: " + host + " at port " + port);
		
    }

    /** Return host for which this request is intended */
    public String getHost() {
    	return host;
    }

    /** Return port for server */
    public int getPort() {
    	return port;
    }
    
    /*
     * 判定暫存用
     */
    public String getURI() {
    	return URI;
    }

    /**
     * Convert request into a string for easy re-sending.
     */
    public String toString() {
		String req = "";
	
		req = method + " " + URI + " " + version + CRLF;
		req += headers;
		/* This proxy does not support persistent connections */
		req += "Connection: close" + CRLF;
		req += CRLF;
		
		return req;
    }
}
