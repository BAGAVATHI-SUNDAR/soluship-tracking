package com.fedex;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.fedex.ws.track.v14.CarrierCodeType;
import com.fedex.ws.track.v14.ClientDetail;
import com.fedex.ws.track.v14.CompletedTrackDetail;
import com.fedex.ws.track.v14.Notification;
import com.fedex.ws.track.v14.NotificationSeverityType;
import com.fedex.ws.track.v14.TrackDetail;
import com.fedex.ws.track.v14.TrackIdentifierType;
import com.fedex.ws.track.v14.TrackPackageIdentifier;
import com.fedex.ws.track.v14.TrackPortType;
import com.fedex.ws.track.v14.TrackReply;
import com.fedex.ws.track.v14.TrackRequest;
import com.fedex.ws.track.v14.TrackRequestProcessingOptionType;
import com.fedex.ws.track.v14.TrackSelectionDetail;
import com.fedex.ws.track.v14.TrackServiceLocator;
import com.fedex.ws.track.v14.TrackStatusDetail;
import com.fedex.ws.track.v14.TransactionDetail;
import com.fedex.ws.track.v14.VersionId;
import com.fedex.ws.track.v14.WebAuthenticationCredential;
import com.fedex.ws.track.v14.WebAuthenticationDetail;
import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

/** 
 * <p>
 * Client class for Tracking FedEx shipment 
 * <p>
 * <p>
 * Implemented FedEx Track Version : 14
 * <p>
 */ 
public class FedExTrackingClient extends TrackingClient{
	
	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			for (TrackOrder trackOrder : orders) {
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
					try {
						TrackRequest trackRequest = new TrackRequest();
						trackRequest.setClientDetail(createClientDetail());
						trackRequest.setWebAuthenticationDetail(createWebAuthenticationDetail());

						TransactionDetail transactionDetail = new TransactionDetail();
						transactionDetail.setCustomerTransactionId("Soluship - Tracking Request");
						trackRequest.setTransactionDetail(transactionDetail);

						VersionId versionId = new VersionId("trck", 14, 0, 0);
						trackRequest.setVersion(versionId);
						TrackSelectionDetail selectionDetail=new TrackSelectionDetail();
				        TrackPackageIdentifier packageIdentifier=new TrackPackageIdentifier();
				        packageIdentifier.setType(TrackIdentifierType.TRACKING_NUMBER_OR_DOORTAG);
				        packageIdentifier.setValue(trackOrder.getTrackingNumber()); // tracking number
				        logs.add("FedEx Package Identifier type : "+packageIdentifier.getType());
				        logs.add("FedEx Package Identifier value : "+packageIdentifier.getValue());
				        selectionDetail.setPackageIdentifier(packageIdentifier);
				        selectionDetail.setCarrierCode(getCarrierCodeType());
				        logs.add("FedEx Carrier code : "+selectionDetail.getCarrierCode());
				        trackRequest.setSelectionDetails(new TrackSelectionDetail[] {selectionDetail});
				        TrackRequestProcessingOptionType processingOption=TrackRequestProcessingOptionType.INCLUDE_DETAILED_SCANS;
				        trackRequest.setProcessingOptions(new TrackRequestProcessingOptionType[]{processingOption});
				        TrackServiceLocator service = new TrackServiceLocator();
				        service.setTrackServicePortEndpointAddress("https://ws.fedex.com:443/web-services");
				        TrackPortType port = service.getTrackServicePort();
				        TrackReply reply = port.track(trackRequest);
				        if (isResponseOk(reply)){
				        	CompletedTrackDetail[] completedTrackDetails=reply.getCompletedTrackDetails();
				        	if(completedTrackDetails!=null){
				        		for(CompletedTrackDetail completedTrackDetail:completedTrackDetails){
				        			TrackDetail td[] = completedTrackDetail.getTrackDetails();
				        			for (int i=0; i< td.length; i++) {
				        				TrackStatusDetail trackingDetail = td[i].getStatusDetail();
				        				logs.add("FedEx Tracking Status Code : "+trackingDetail.getCode());
				        				logs.add("FedEx Tracking Status Description : "+trackingDetail.getDescription());
				        				if(trackingDetail.getCode()!=null) {
				        					APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
											apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
											apiShipmentStatus.setApiStatusCode(trackingDetail.getCode());
											apiShipmentStatus.setApiStatusDesc(trackingDetail.getDescription());
											setCurrentOrderStatus(trackOrder,apiShipmentStatus);
				        				}
				        			}
				        			break;
				        		}
				        	}
				        }
					} catch (RemoteException | ServiceException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return orders;
	}
	
	public ClientDetail createClientDetail() {
		ClientDetail clientDetail = new ClientDetail();   
        clientDetail.setAccountNumber("354975017");
        clientDetail.setMeterNumber("105010237");
        return clientDetail;
	}
	
	public WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential webAuthenticationCredential = new WebAuthenticationCredential();
        webAuthenticationCredential.setKey("mP52jK4iAHC1zHBC");
        webAuthenticationCredential.setPassword("JpfhiTYTi88fcUOFRbJdFcv3P");
        WebAuthenticationDetail webAuthenticationDetail=new WebAuthenticationDetail();
        webAuthenticationDetail.setUserCredential(webAuthenticationCredential);
		return webAuthenticationDetail;
	}
	
	private CarrierCodeType getCarrierCodeType() {
		/*if(order.getService()!=null){
			if(order.getService().getCode().contains("GROUND")){
				return CarrierCodeType.FDXG;
			}else if(order.getService().getCode().contains("EXPRESS")){
				return CarrierCodeType.FDXE;
			}else if(order.getService().getCode().contains("FREIGHT")){
				return CarrierCodeType.FXFR;
			}else if(order.getService().getCode().contains("SMART")){
				return CarrierCodeType.FXSP;
			}else{
				//Default is Express
				return CarrierCodeType.FDXE;
			}
		}else{
			//Default is Express
			return CarrierCodeType.FDXE;
		}*/
		return CarrierCodeType.FDXE;
	}

	private boolean isResponseOk(TrackReply reply) {
		boolean isValidResponse=false;
		if (reply != null) {
			logs.add("FedEx Response : TrackReply");
			if(reply.getHighestSeverity()!=null) {
				logs.add("FedEx Response Status: "+reply.getHighestSeverity());
				if(!reply.getHighestSeverity().equals(NotificationSeverityType.ERROR) && !reply.getHighestSeverity().equals(NotificationSeverityType.FAILURE)){
					isValidResponse=true;
				}
			}
			if(reply.getNotifications()!=null && reply.getNotifications().length>0){
				logs.add("FedEx Notification: ");
				int i=1;
				for(Notification notification:reply.getNotifications()){
					if(!isValidResponse){
						errorMessages.add(notification.getMessage());
					}
					logs.add(i+" -> "+notification.getMessage());
					i++;
				}
			}
		}
		return isValidResponse; 
	}

}