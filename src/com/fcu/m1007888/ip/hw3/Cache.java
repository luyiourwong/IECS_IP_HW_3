package com.fcu.m1007888.ip.hw3;

import java.io.Serializable;

public class Cache implements Serializable{
	
	private static final long serialVersionUID = 1L;

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
