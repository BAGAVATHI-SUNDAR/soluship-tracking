package com.soluship.tracking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.ApexTrackingClient;
import com.csatransport.CSATransportTrackingClient;
import com.dayandross.DayAndRossTrackingClient;
import com.dhl.DHLTrackingClient;
import com.fedex.FedExTrackingClient;
import com.loomis.LoomisTrackingClient;
import com.purolator.PurolatorTrackingClient;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.bean.TrackingRequest;
import com.soluship.tracking.bean.TrackingResponse;
import com.soluship.tracking.mapper.DBMapper;
import com.soluship.tracking.service.TrackingClient;
import com.ups.UPSTrackingClient;
import com.westerncanada.WesternCanadaTrackingClient;

/**
 * <h1>EDI Service Controller!</h1>
 * <br>
 * This is a Spring Controller class which handles REST requests related to Tracking Service<br><br>
 * - Configured URL <b>'#soluship-billing-context#/v1/rest/track'</b><br>
 * - Configured Method <b>GET</b>,<b>POST</b>,<b>PUT</b><br>
 * <br>
 * @author sbsundar
 * @version 1.0
 * @since   2019-07-17
 */
@RestController
@RequestMapping("/v1/rest/track")
public class SolushipTrackingService {
	
	private static final Logger logger = LoggerFactory.getLogger(SolushipTrackingService.class);

	@Autowired
	private DBMapper dbMapper;
	
	/**
	 * <b>Method for handling API request</b> - To get current order status<br><br>
	 * <b>Configured Method</b> 	-> POST	<br>
	 * <b>Configured URL</b> 		->'#soluship-billing-context#/#controller-context#/current-order-status'<br>
	 * @return {@link com.soluship.billing.service.api.client.model.TrackingResponse}
	 */
	@PostMapping("/current-order-status")
	public TrackingResponse currentOrderStatus(@RequestBody TrackingRequest trackingRequest){
		TrackingResponse trackingResponse=new TrackingResponse();
		List<TrackOrder> trackedOrders=new ArrayList<>();
		logger.debug("Processing Request /current-order-status");
		if(trackingRequest!=null && trackingRequest.getTrackOrders()!=null && !trackingRequest.getTrackOrders().isEmpty()) {
			List<Long> carrierIds=trackingRequest.getTrackOrders().stream().map(TrackOrder::getCarrierId).distinct().collect(Collectors.toList());
			if(carrierIds!=null && !carrierIds.isEmpty()) {
				List<Thread> calledThreads=new ArrayList<>();
				for(long carrierId:carrierIds) {
					Thread t = new Thread(
							new Runnable() {
								public void run () {
									List<TrackOrder> orders=trackingRequest.getTrackOrders().stream()
			    							.filter((trackOrder)-> trackOrder.getCarrierId()==carrierId).collect(Collectors.toList());
			    					if(orders!=null && !orders.isEmpty()) {
			    						orders=trackCurrentOrderStatus(carrierId,orders);
			    						trackedOrders.addAll(orders);
			    					}
								}
							}
					);
					t.start();
					calledThreads.add(t);
				}
				for (Thread thread : calledThreads) {
					try {
						thread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		trackingResponse.setTrackOrders(trackedOrders);
		logger.debug("Processed Request /current-order-status");
		return trackingResponse;
	}
	
	private List<TrackOrder> trackCurrentOrderStatus(long carrierId, List<TrackOrder> orders) {
		try {
			TrackingClient trackingClient=null;
			if(carrierId==1l) {
				trackingClient=new FedExTrackingClient();
			}else if(carrierId==2l || carrierId==5l) {
				trackingClient=new UPSTrackingClient();
			}else if(carrierId==3l) {
				trackingClient=new DHLTrackingClient();
			}else if(carrierId==4l) {
				trackingClient=new LoomisTrackingClient();
			}else if(carrierId==20l) {
				trackingClient=new PurolatorTrackingClient();
			}else if(carrierId==1008l) {
				trackingClient=new DayAndRossTrackingClient();
			}else if(carrierId==1002l) {
				trackingClient=new ApexTrackingClient();
			}else if(carrierId==2156l){
				trackingClient=new CSATransportTrackingClient();
			}else if(carrierId==1003l){
				trackingClient=new WesternCanadaTrackingClient();
			}else {
				return orders;
			}
			trackingClient.configureInstance(dbMapper);
			trackingClient.trackCurrentOrderStatus(orders);
			trackingClient.printConsolidatedLog();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return orders;
	}

	public static void main(java.lang.String args[]){
    	List<TrackOrder> trackOrders=new ArrayList<>();
    	
    	TrackOrder trackOrder=new TrackOrder();
    	/*trackOrder.setCarrierId(1008);
    	trackOrder.setTrackingNumber("TOR6200003");//DET1938802//PTH0135564
    	trackOrder.setOrderId(2322107);
    	trackOrders.add(trackOrder);*/
    	
    	/*trackOrder.setCarrierId(1002);
    	trackOrder.setTrackingNumber("2337115");//2337115//1250962151
    	trackOrder.setOrderId(2322107);
    	trackOrders.add(trackOrder);*/
    	
    	/*trackOrder.setCarrierId(2156);
    	trackOrder.setTrackingNumber("C0613348");//C0613348//C0699591
    	trackOrder.setOrderId(2322107);
    	trackOrders.add(trackOrder);*/
    	
    	trackOrder.setCarrierId(1003);
    	trackOrder.setTrackingNumber("1251263361");//1251263361//2328366
    	trackOrder.setOrderId(2322107);
    	trackOrders.add(trackOrder);
    	
    	TrackingRequest trackingRequest=new TrackingRequest();
    	trackingRequest.setTrackOrders(trackOrders);
    	SolushipTrackingService solushipTrackingService=new SolushipTrackingService();
		TrackingResponse trackingResponse = solushipTrackingService.currentOrderStatus(trackingRequest);
		if(trackingResponse!=null) {
			
		}
    	
    }
}
