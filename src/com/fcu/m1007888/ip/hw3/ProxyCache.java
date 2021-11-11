package com.fcu.m1007888.ip.hw3;

/**
 * ProxyCache.java - Simple caching proxy
 *
 * $Id: ProxyCache.java,v 1.3 2004/02/16 15:22:00 kangasha Exp $
 *
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {
    /** Port for the proxy */
    private static int port;
    /** Socket for client connections */
    private static ServerSocket socket;

    /** Create the ProxyCache object and the socket */
    public static void init(int p) {
		port = p;
		try {
		    socket = new ServerSocket(port); /* Fill in, 伺服器的socket, 使用port的port */
		    System.out.println("[init] 建立了server socket: " + port);
		} catch (IOException e) {
		    System.out.println("Error creating socket: " + e);
		    System.exit(-1);
		}
    }

    public static void handle(Socket client) {
		Socket server = null;
		HttpRequest request = null;
		HttpResponse response = null;
	
		/* Process request. If there are any exceptions, then simply
		 * return and end this request. This unfortunately means the
		 * client will hang for a while, until it timeouts. */
	
		/* Read request */
		try {
		    BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); /* Fill in, 取德client socket 的input*/
		    request = new HttpRequest(fromClient); /* Fill in, 從client buffer拿 */
		    if(request.isERROR()) {
		    	System.out.println("[handle] 讀取請求失敗, 內容為空");
		    	return;
		    }
		    System.out.println("[handle] 成功讀取請求");
		} catch (IOException e) {
		    System.out.println("Error reading request from client: " + e);
		    return;
		}
		/* Send request to server */
		try {
		    /* Open socket and write request to socket */
		    server = new Socket(request.getHost(), request.getPort()); /* Fill in, 建立socket到要求的網址 */
		    DataOutputStream toServer = new DataOutputStream(server.getOutputStream()); /* Fill in */
		    toServer.writeBytes(request.toString()); /* Fill in, 寫入要求的內容 */
		    System.out.println("[handle] 傳送請求至伺服器: " + request.getHost() + ":" + request.getPort());
		} catch (UnknownHostException e) {
		    System.out.println("Unknown host: " + request.getHost());
		    System.out.println(e);
		    return;
		} catch (IOException e) {
		    System.out.println("Error writing request to server: " + e);
		    return;
		}
		/* Read response and forward it to client */
		try {
		    DataInputStream fromServer = new DataInputStream(server.getInputStream()); /* Fill in, 伺服器的input */
		    response = new HttpResponse(fromServer); /* Fill in, 伺服器的回應 */
		    System.out.println("[handle] 取得伺服器回應:");
		    System.out.println(response.toString());
		    DataOutputStream toClient = new DataOutputStream(client.getOutputStream()); /* Fill in, 客戶端的input */
		    toClient.writeBytes(response.toString());
            toClient.write(response.body); /* Fill in, 傳送伺服器的回應給客戶端 */
            System.out.println("[handle] 傳送伺服器回應給客戶端");
		    /* Write response to client. First headers, then body */
		    client.close();
		    server.close();
		    System.out.println("[handle] 關閉連線");
		    /* Insert object into the cache */
		    
		    /* Fill in (optional exercise only) */
		} catch (IOException e) {
		    System.out.println("Error writing response to client: " + e);
		}
    }


    /** Read command line arguments and start proxy */
    public static void main(String args[]) {
		int myPort = 0;
		
		try {
		    myPort = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
		    System.out.println("Need port number as argument");
		    System.exit(-1);
		} catch (NumberFormatException e) {
		    System.out.println("Please give port number as integer.");
		    System.exit(-1);
		}
		
		System.out.println("[init] 開始初始化");
		init(myPort);
		System.out.println("[init] 初始化完畢, 等待連線");
	
		/** Main loop. Listen for incoming connections and spawn a new
		 * thread for handling them */
		Socket client = null;
		
		while (true) {
		    try {
				client = socket.accept(); /* Fill in, 接受到的socket */
				System.out.println("[accept] 接受了新的client");
				handle(client);
		    } catch (IOException e) {
				System.out.println("Error reading request from client: " + e);
				/* Definitely cannot continue processing this request,
				 * so skip to next iteration of while loop. */
				continue;
		    }
		}
    }
}
