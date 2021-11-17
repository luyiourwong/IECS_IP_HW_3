package com.fcu.m1007888.ip.hw3;

import java.io.Serializable;

public class Cache implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * 暫存檔
	 * 其實就只有存URI跟Response
	 * 獨立一個檔案是為了讓檔案名稱可以亂取不用做轉換
	 * 因為原始URI有包含資料夾禁字，這樣做之後名稱就能隨便轉換，反正讀取的時候是讀內部的URI
	 * 
	 * 至於Response
	 * 因為網頁內容都存在Response裡面
	 * 所以把整個Response存起來就好，要用的時候拿出來
	 * 
	 */

	private String URI;
	private HttpResponse resp;
	
	public Cache(String URI, HttpResponse resp) {
		this.URI = URI;
		this.resp = resp;
	}

	public String getURI() {
		return URI;
	}

	public void setURI(String URI) {
		this.URI = URI;
	}

	public HttpResponse getResp() {
		return resp;
	}

	public void setResp(HttpResponse resp) {
		this.resp = resp;
	}
	
	/*
	 * 資料夾禁字處理，只有存檔時用，所以全部轉成同一個就好
	 */
	public String getUID() {
		String uid = getURI();
		uid = uid.replace(":", "_");
		uid = uid.replace("/", "_");
		uid = uid.replace(".", "_");
		uid = uid.replace("?", "_");
		uid = uid.replace("*", "_");
		uid = uid.replace("<", "_");
		uid = uid.replace(">", "_");
		uid = uid.replace("|", "_");
		return uid;
	}
	
	@Override
	public String toString() {
		return this.getUID();
	}
}
