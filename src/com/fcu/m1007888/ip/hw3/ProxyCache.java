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
	
	/*
	 * 測試網頁: 是http, 有文字, 有圖片
	 * 
	 * http://ms2.iecs.fcu.edu.tw/mailman/listinfo/
	 * 
	 */
	
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
		    	System.out.println("[handle] 讀取請求失敗, 切斷連線");
		    	client.close();
		    	return;
		    }
		    System.out.println("[handle] 成功讀取請求");
		} catch (IOException e) {
		    System.out.println("Error reading request from client: " + e);
		    return;
		}
		
		/* Send request to server */
		if(hasCache(request.getURI())) {
			/*
			 * 有cache的方法
			 */
			System.out.println("[handle] 有cache, 讀取暫存");
			response = loadCache(request.getURI()).getResp();
		} else {
			/*
			 * 沒有cache的方法
			 */
			System.out.println("[handle] 沒有cache, 傳送請求");
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
			try {
				DataInputStream fromServer = new DataInputStream(server.getInputStream());
				response = new HttpResponse(fromServer); /* Fill in, 伺服器的回應 */
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
		/* Read response and forward it to client */
		try {
		    System.out.println("[handle] 取得伺服器回應:");
		    System.out.println(response.toString());
		    /* Write response to client. First headers, then body */
		    DataOutputStream toClient = new DataOutputStream(client.getOutputStream()); /* Fill in, 客戶端的input */
		    toClient.writeBytes(response.toString());
            toClient.write(response.body); /* Fill in, 傳送伺服器的回應給客戶端 */
            System.out.println("[handle] 傳送伺服器回應給客戶端");
            /* Insert object into the cache */
		    /* Fill in (optional exercise only) */
		    saveCache(request.getURI(), response);
		    
		    client.close();
		    if(server != null)server.close();
		    System.out.println("[handle] 關閉連線");
		    
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
		initCache();
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
    
    /*
     * Cache
     */
    
    public static Map<String, Cache> mapCache;
    
    public static void initCache() {
    	mapCache = new HashMap<String, Cache>();
    	System.out.println("[cache] 初始化cache");
    	File folder = new File("cache");
		if (!folder.exists()) {
			folder.mkdir();
			System.out.println("[cache] 建立資料夾");
		}
		for (File f : folder.listFiles()) {
			Cache cache = loadCacheFromFile(f);
			mapCache.put(cache.getURI(), cache);
			System.out.println("[cache] 讀取cache: " + cache.getURI());
		}
    }
    
    public static void saveCache(String URI, HttpResponse resp) {
    	Cache cache = new Cache(URI, resp);
    	mapCache.put(URI, cache);
    	saveCachetoFile(cache);
    	System.out.println("[cache] 新增 cache: " + URI);
    }
    
    public static Cache loadCache(String URI) {
    	if(hasCache(URI)) {
    		return mapCache.get(URI);
    	}
    	return null;
    }
    
    public static boolean hasCache(String URI) {
    	return mapCache.containsKey(URI);
    }
    
    public static Cache loadCacheFromFile(File file) {
		try {
			FileInputStream fi = new FileInputStream(file);
			ObjectInputStream oi = new ObjectInputStream(fi);
			
			Cache cache = (Cache) oi.readObject();
			
			oi.close();
			fi.close();
			
			return cache;
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
    public static void saveCachetoFile(Cache cache) {
		try {
			FileOutputStream fo = new FileOutputStream(new File("cache" + File.separator + "" + cache.toString() + ".data"));
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			
			oo.writeObject(cache);
			
			oo.close();
			fo.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
