package com.purolator;

import java.util.List;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;

import com.purolator.pws.service.v1.TrackingServiceStub;
import com.purolator.pws.service.v1.TrackingServiceStub.ArrayOfPIN;
import com.purolator.pws.service.v1.TrackingServiceStub.ArrayOfScan;
import com.purolator.pws.service.v1.TrackingServiceStub.ArrayOfTrackingInformation;
import com.purolator.pws.service.v1.TrackingServiceStub.Language;
import com.purolator.pws.service.v1.TrackingServiceStub.PIN;
import com.purolator.pws.service.v1.TrackingServiceStub.RequestContext;
import com.purolator.pws.service.v1.TrackingServiceStub.RequestContextE;
import com.purolator.pws.service.v1.TrackingServiceStub.Scan;
import com.purolator.pws.service.v1.TrackingServiceStub.TrackPackagesByPinRequest;
import com.purolator.pws.service.v1.TrackingServiceStub.TrackPackagesByPinRequestContainer;
import com.purolator.pws.service.v1.TrackingServiceStub.TrackPackagesByPinResponse;
import com.purolator.pws.service.v1.TrackingServiceStub.TrackPackagesByPinResponseContainer;
import com.purolator.pws.service.v1.TrackingServiceStub.TrackingInformation;
import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

public class PurolatorTrackingClient extends TrackingClient{
	
	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			try {
    			TrackingServiceStub stub =new TrackingServiceStub("https://webservices.purolator.com/PWS/V1/Tracking/TrackingService.asmx");

        		Options options = stub._getServiceClient().getOptions();
        		HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
        		auth.setUsername("57e6f3e8d9c44bc788358c2728b09ae6");
        		auth.setPassword("Nqf%XaZ^");		
        		options.setProperty(HTTPConstants.AUTHENTICATE,auth);
        		
        		RequestContextE requestContext = new RequestContextE();
        		RequestContext requestContextBody = new RequestContext();
        		requestContextBody.setVersion("1.1");
        		requestContextBody.setLanguage(Language.en);
        		requestContextBody.setGroupID(null);
        		requestContextBody.setRequestReference("RequestReference");
        		requestContext.setRequestContext(requestContextBody);
        		
    			TrackPackagesByPinRequest request = new TrackPackagesByPinRequest();
    			TrackPackagesByPinRequestContainer reqContainer = new TrackPackagesByPinRequestContainer();
    		
    			PIN[] pins = new PIN[orders.size()];
    			int i=0;
    			for(TrackOrder trackOrder:orders) {
    				PIN pin = new PIN();
    				pin.setValue(trackOrder.getTrackingNumber());
    				pins[i]=pin;
    				i++;
    			}
    			ArrayOfPIN arr = new ArrayOfPIN();
    			arr.setPIN(pins);
    			reqContainer.setPINs(arr);
    			request.setTrackPackagesByPinRequest(reqContainer);
    		
    			// Call the service
    			TrackPackagesByPinResponse response = stub.TrackPackagesByPin(request,requestContext);
    			TrackPackagesByPinResponseContainer trackRespContainer = response.getTrackPackagesByPinResponse();
    		
    			if(trackRespContainer!=null && trackRespContainer.getResponseInformation()!=null){
    				ArrayOfTrackingInformation trackingInformationList = trackRespContainer.getTrackingInformationList();
    				if (trackingInformationList != null && trackingInformationList.getTrackingInformation()!=null){
    					for(TrackingInformation trackingInformation : trackingInformationList.getTrackingInformation()){
    						ArrayOfScan scans = trackingInformation.getScans();
    						if(scans!=null && scans.getScan()!=null && scans.getScan().length>0){
    							Scan[] scan = scans.getScan();
    							for (TrackOrder trackOrder : orders) {	
    								if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty() 
    										&& trackOrder.getTrackingNumber().equalsIgnoreCase(scan[0].getPIN().getValue())) {
    									logs.add("TRACKING NO : "+scan[0].getPIN().getValue());
    	    							logs.add("CODE : "+scan[0].getDescription());
    	    							logs.add("DESCRIPTION : "+scan[0].getDescription());
    	    							APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
    	    							apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
    	    							apiShipmentStatus.setApiStatusCode(scan[0].getDescription());
    	    							apiShipmentStatus.setApiStatusDesc(scan[0].getDescription());
    	    							setCurrentOrderStatus(trackOrder,apiShipmentStatus);
    	    							break;
    								}
    							}
    						}
    					}
    				}else {
    					if(trackRespContainer.getResponseInformation().getErrors()!=null && trackRespContainer.getResponseInformation().getErrors().getError()!=null){
    						
    					}	
    				}
    			}	
        		
    		} catch(Exception e){
    			e.printStackTrace();
    			errorMessages.add(e.getMessage());
    		}
		}
		return orders;
	}
    
    public void getOrderTrackingHistoryForTrackingNumbers(List<String> trackingNumbers){
    	if(trackingNumbers!=null && !trackingNumbers.isEmpty()) {
    		try {
    			TrackingServiceStub stub =new TrackingServiceStub("https://webservices.purolator.com/PWS/V1/Tracking/TrackingService.asmx");

        		Options options = stub._getServiceClient().getOptions();
        		HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
        		auth.setUsername("57e6f3e8d9c44bc788358c2728b09ae6");
        		auth.setPassword("Nqf%XaZ^");		
        		options.setProperty(HTTPConstants.AUTHENTICATE,auth);
        		
        		RequestContextE requestContext = new RequestContextE();
        		RequestContext requestContextBody = new RequestContext();
        		requestContextBody.setVersion("1.1");
        		requestContextBody.setLanguage(Language.en);
        		requestContextBody.setGroupID(null);
        		requestContextBody.setRequestReference("RequestReference");
        		requestContext.setRequestContext(requestContextBody);
        		
    			TrackPackagesByPinRequest request = new TrackPackagesByPinRequest();
    			TrackPackagesByPinRequestContainer reqContainer = new TrackPackagesByPinRequestContainer();
    		
    			PIN[] pins = new PIN[trackingNumbers.size()];
    			int i=0;
    			for(String trackingNumber:trackingNumbers) {
    				PIN pin = new PIN();
    				pin.setValue(trackingNumber);
    				pins[i]=pin;
    				i++;
    			}
    			ArrayOfPIN arr = new ArrayOfPIN();
    			arr.setPIN(pins);
    			reqContainer.setPINs(arr);
    			request.setTrackPackagesByPinRequest(reqContainer);
    		
    			// Call the service
    			TrackPackagesByPinResponse response = stub.TrackPackagesByPin(request,requestContext);
    			TrackPackagesByPinResponseContainer trackRespContainer = response.getTrackPackagesByPinResponse();
    		
    			if(trackRespContainer!=null && trackRespContainer.getResponseInformation()!=null){
    				ArrayOfTrackingInformation trackingInformationList = trackRespContainer.getTrackingInformationList();
    				if (trackingInformationList != null && trackingInformationList.getTrackingInformation()!=null){
    					for(TrackingInformation trackingInformation : trackingInformationList.getTrackingInformation()){
    						ArrayOfScan scans = trackingInformation.getScans();
    						if(scans!=null && scans.getScan()!=null && scans.getScan().length>0){
    							Scan[] scan = scans.getScan();
    							logs.add("TRACKING NO : "+scan[0].getPIN().getValue());
    							logs.add("CODE : "+scan[0].getDescription());
    							logs.add("DESCRIPTION : "+scan[0].getDescription());
    						}
    					}
    				}else {
    					if(trackRespContainer.getResponseInformation().getErrors()!=null && trackRespContainer.getResponseInformation().getErrors().getError()!=null){
    						
    					}	
    				}
    			}	
        		
    		} catch(Exception e){
    			e.printStackTrace();
    			errorMessages.add(e.getMessage());
    		}
    	}
    }
}
