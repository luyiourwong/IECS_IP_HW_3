package com.fcu.m1007888.ip.hw3;

public class Cache {

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
}
