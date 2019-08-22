package com.soluship.tracking.bean;

public class TrackOrder {
	private long orderId;
	private String trackingNumber;
	private long carrierId;
	private OrderStatus currentOrderStatus;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	public long getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}
	public OrderStatus getCurrentOrderStatus() {
		return currentOrderStatus;
	}
	public void setCurrentOrderStatus(OrderStatus currentOrderStatus) {
		this.currentOrderStatus = currentOrderStatus;
	}
}
