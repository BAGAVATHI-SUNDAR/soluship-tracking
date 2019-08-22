package com.loomis;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.axis.AxisFault;

import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

import ca.transforce.uss.addons.ws.USSAddonsServiceSoap11BindingStub;
import ca.transforce.uss.addons.ws.xsd.TrackByBarcodeRq;
import ca.transforce.uss.addons.ws.xsd.TrackByBarcodeRs;
import ca.transforce.uss.dto.xsd.TrackingEvent;
import ca.transforce.uss.dto.xsd.TrackingResult;

public class LoomisTrackingClient extends TrackingClient{

	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			for (TrackOrder trackOrder : orders) {
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
					TrackByBarcodeRq tbbrequest = new TrackByBarcodeRq();
					tbbrequest.setBarcode(trackOrder.getTrackingNumber());
					tbbrequest.setTrack_shipment(true);
					java.net.URL endpointURL = null;
					try {
						endpointURL = new java.net.URL("https://webservice.loomis-express.com/LShip/services/USSAddonsService?wsdl");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					javax.xml.rpc.Service service = null;
					USSAddonsServiceSoap11BindingStub req = null;
					try {
						req = new USSAddonsServiceSoap11BindingStub(endpointURL,service);
					} catch (AxisFault e) {
						e.printStackTrace();
					}
					TrackByBarcodeRs resp = null;
					try {
						resp = req.trackByBarcode(tbbrequest);
						if(resp.getError() !=null){
							String respInf = resp.getError();
							if(respInf.length() > 0){
								errorMessages.add(respInf);
							}
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					if(resp !=null && resp.getResult() !=null){
						TrackingResult[] trackResult = resp.getResult();
						if(trackResult !=null && trackResult[0] != null){
							TrackingEvent trackingEvent = trackResult[0].getEvents(0);
							if(trackingEvent !=null){
								logs.add("CODE : "+trackingEvent.getCode());
								logs.add("DESCRIPTION : "+trackingEvent.getCode_description_en());
								APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
    							apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
    							apiShipmentStatus.setApiStatusCode(trackingEvent.getCode());
    							apiShipmentStatus.setApiStatusDesc(trackingEvent.getCode_description_en());
    							setCurrentOrderStatus(trackOrder,apiShipmentStatus);
							}
						}
					}
				}
			}
		}
		return orders;
	}
}
