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
    /* POST */
    boolean isPOST = false;
    String ContentType;
    int ContentLength;
    byte[] body = new byte[MAX_OBJECT_SIZE];
    public boolean isPOST() {
    	return isPOST;
    }
    
    private boolean ERROR = false;
    public boolean isERROR() {
    	return ERROR;
    }

    /** Create HttpRequest by reading it from the client socket */
    public HttpRequest(BufferedReader from) {
    	System.out.println("[HttpRequest] �}�l�إ߽ШD");
		String firstLine = "";
		try {
		    firstLine = from.readLine();
		    System.out.println("[HttpRequest] Ū�����J");
		} catch (IOException e) {
		    System.out.println("Error reading request line: " + e);
		}
		
		if(firstLine == null || firstLine.isEmpty() || firstLine.isBlank()) {
			ERROR = true;
			return;
		}
		
		/* Fill in 
		 * 
		 * ���e�榡��:
		 * GET go.microsoft.com:443 HTTP/1.0
		 * 
		 * */
		System.out.println("firstLine is: " + firstLine);
		String[] tmp = firstLine.split(" ");
		method = tmp[0];
		URI = tmp[1];
		version = tmp[2];
	
		System.out.println("URI is: " + URI);
	
		if (!method.equals("GET")) {
		    System.out.println("[Request] Warning: Method not GET");
		}
		if (method.equals("CONNECT")) {
		    System.out.println("[Request] Error: Method CONNECT");
		    ERROR = true;
			return;
		}
		if (method.equals("POST")) {
			isPOST = true;
		    System.out.println("[Request] POST:");
		}
		try {
			boolean isBody = false;
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
				 * �p�G���Content-Type:�}�Y�A�Ⱶ�U�Ӫ����g�ibody
				 */
				if (isPOST) {
					System.out.println("" + line);
					if (isBody) {
						
					}
					if (line.startsWith("Content-Type:")) {
						
					}
					if (line.startsWith("Content-Length:")) {
						isBody = true;
					}
				}
				/*
				 * ����
				 */
				line = from.readLine();
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
     * �Ȧs��
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
		if(!isPOST) {
			req += "Connection: close" + CRLF;
		}
		req += CRLF;
		
		return req;
    }
}
