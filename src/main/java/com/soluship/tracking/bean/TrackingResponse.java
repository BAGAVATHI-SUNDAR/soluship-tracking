package com.soluship.tracking.bean;

import java.util.List;

public class TrackingResponse {
	private List<TrackOrder> trackOrders;

	public List<TrackOrder> getTrackOrders() {
		return trackOrders;
	}

	public void setTrackOrders(List<TrackOrder> trackOrders) {
		this.trackOrders = trackOrders;
	}

}
