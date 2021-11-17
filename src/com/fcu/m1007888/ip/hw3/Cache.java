package com.fcu.m1007888.ip.hw3;

import java.io.Serializable;

public class Cache implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * �Ȧs��
	 * ���N�u���sURI��Response
	 * �W�ߤ@���ɮ׬O���F���ɮצW�٥i�H�è����ΰ��ഫ
	 * �]����lURI���]�t��Ƨ��T�r�A�o�˰�����W�ٴN���H�K�ഫ�A�ϥ�Ū�����ɭԬOŪ������URI
	 * 
	 * �ܩ�Response
	 * �]���������e���s�bResponse�̭�
	 * �ҥH����Response�s�_�ӴN�n�A�n�Ϊ��ɭԮ��X��
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
	 * ��Ƨ��T�r�B�z�A�u���s�ɮɥΡA�ҥH�����ন�P�@�ӴN�n
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
