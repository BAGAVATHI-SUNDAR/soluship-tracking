package com.soluship.tracking.bean;

public class APIShipmentStatus {
	private int id;
	private long carrierId;
	private String apiStatusCode;
	private String apiStatusDesc;
	private int orderStatusId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}
	public String getApiStatusCode() {
		return apiStatusCode;
	}
	public void setApiStatusCode(String apiStatusCode) {
		this.apiStatusCode = apiStatusCode;
	}
	public String getApiStatusDesc() {
		return apiStatusDesc;
	}
	public void setApiStatusDesc(String apiStatusDesc) {
		this.apiStatusDesc = apiStatusDesc;
	}
	public int getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(int orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
}
