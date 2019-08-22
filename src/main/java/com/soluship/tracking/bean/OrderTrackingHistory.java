package com.soluship.tracking.bean;

public class OrderTrackingHistory {

	private int id;
	private long orderId;
	private long carrierId;
	private APIShipmentStatus apiShipmentStatus;
	private OrderStatus orderStatus;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public long getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}
	public APIShipmentStatus getApiShipmentStatus() {
		return apiShipmentStatus;
	}
	public void setApiShipmentStatus(APIShipmentStatus apiShipmentStatus) {
		this.apiShipmentStatus = apiShipmentStatus;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
