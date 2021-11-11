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
		    socket = new ServerSocket(port); /* Fill in, ���A����socket, �ϥ�port��port */
		    System.out.println("[init] �إߤFserver socket: " + port);
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
		    BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream())); /* Fill in, ���wclient socket ��input*/
		    request = new HttpRequest(fromClient); /* Fill in, �qclient buffer�� */
		    if(request.isERROR()) {
		    	System.out.println("[handle] Ū���ШD����, ���e����");
		    	return;
		    }
		    System.out.println("[handle] ���\Ū���ШD");
		} catch (IOException e) {
		    System.out.println("Error reading request from client: " + e);
		    return;
		}
		/* Send request to server */
		try {
		    /* Open socket and write request to socket */
		    server = new Socket(request.getHost(), request.getPort()); /* Fill in, �إ�socket��n�D�����} */
		    DataOutputStream toServer = new DataOutputStream(server.getOutputStream()); /* Fill in */
		    toServer.writeBytes(request.toString()); /* Fill in, �g�J�n�D�����e */
		    System.out.println("[handle] �ǰe�ШD�ܦ��A��: " + request.getHost() + ":" + request.getPort());
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
		    DataInputStream fromServer = new DataInputStream(server.getInputStream()); /* Fill in, ���A����input */
		    response = new HttpResponse(fromServer); /* Fill in, ���A�����^�� */
		    System.out.println("[handle] ���o���A���^��:");
		    System.out.println(response.toString());
		    DataOutputStream toClient = new DataOutputStream(client.getOutputStream()); /* Fill in, �Ȥ�ݪ�input */
		    toClient.writeBytes(response.toString());
            toClient.write(response.body); /* Fill in, �ǰe���A�����^�����Ȥ�� */
            System.out.println("[handle] �ǰe���A���^�����Ȥ��");
		    /* Write response to client. First headers, then body */
		    client.close();
		    server.close();
		    System.out.println("[handle] �����s�u");
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
		
		System.out.println("[init] �}�l��l��");
		init(myPort);
		System.out.println("[init] ��l�Ƨ���, ���ݳs�u");
	
		/** Main loop. Listen for incoming connections and spawn a new
		 * thread for handling them */
		Socket client = null;
		
		while (true) {
		    try {
				client = socket.accept(); /* Fill in, �����쪺socket */
				System.out.println("[accept] �����F�s��client");
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
