package com.ups;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpStatus;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

import com.cwsi.eshipper.carrier.ups.track.ActivityDocument.Activity;
import com.cwsi.eshipper.carrier.ups.track.ShipmentDocument.Shipment;
import com.cwsi.eshipper.carrier.ups.track.TrackRequestDocument;
import com.cwsi.eshipper.carrier.ups.track.TrackRequestDocument.TrackRequest;
import com.cwsi.eshipper.carrier.ups.track.TrackResponseDocument;
import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

public class UPSTrackingClient extends TrackingClient{
	
	@SuppressWarnings("deprecation")
	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			for (TrackOrder trackOrder : orders) {
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {

					PostMethod method = new PostMethod("https://wwwcie.ups.com/ups.app/xml/Track");
					method.setRequestHeader("Referer","Canada Worldwide Services");
			        method.setRequestHeader("Content-type","text/xml");
					try {
						TrackRequestDocument request_doc = TrackRequestDocument.Factory.newInstance();
						TrackRequest request = request_doc.addNewTrackRequest();
		
						request.addNewRequest().setRequestAction("Track");
						request.setTrackingNumber(trackOrder.getTrackingNumber());
		
						XmlOptions opts = new XmlOptions();
						opts.setSavePrettyPrint();
						opts.setUseDefaultNamespace();
						StringWriter writer = new StringWriter();
						
						request.save(writer,opts);
						
						String requestStr = this.getAccessRequest().toString() + writer.toString();
						
						requestStr = requestStr.replaceAll("xmlns=\\\".*\\\"","");
						requestStr = requestStr.replaceAll("xml-fragment", "TrackRequest");
						
						HttpClient client = new HttpClient();
						client.getParams().setVersion(HttpVersion.HTTP_1_0);
				        method.setRequestEntity(new StringRequestEntity(requestStr));
						int statusCode = client.executeMethod(method);
		
			        	if (statusCode == HttpStatus.SC_OK) {
			        		// Read the response body.
				            String response = method.getResponseBodyAsString();
				            response = response.replaceFirst("<?.*?>","");
				            response = response.replaceAll("res:", "");
				            response = response.replaceFirst(">"," xmlns=\"http://www.cwsi.com/eshipper/carrier/ups/track\">");
				            TrackResponseDocument response_doc = TrackResponseDocument.Factory.parse(response);
				            if(response_doc!=null && response_doc.getTrackResponse()!=null) {
				            	Shipment shipment = response_doc.getTrackResponse().getShipmentList().get(0);
								List<com.cwsi.eshipper.carrier.ups.track.PackageDocument.Package> packages = shipment.getPackageList();
								com.cwsi.eshipper.carrier.ups.track.PackageDocument.Package p = packages.get(0);
								List<Activity> act_list = p.getActivityList();
								for(Activity a: act_list){
									if(a.getStatus()!=null && a.getStatus().getStatusType()!=null && a.getStatus().getStatusType().getCode()!=null) {
										logs.add("CODE : "+a.getStatus().getStatusType().getCode());
										logs.add("DESCRIPTION : "+a.getStatus().getStatusType().getDescription());
										APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
										apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
										apiShipmentStatus.setApiStatusCode(a.getStatus().getStatusType().getCode());
										apiShipmentStatus.setApiStatusDesc(a.getStatus().getStatusType().getDescription());
										setCurrentOrderStatus(trackOrder,apiShipmentStatus);
									}
								}
				            }
			        	}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (XmlException e) {
						e.printStackTrace();
					}
					finally {
			            method.releaseConnection();
			        }	
				
				}
			}
		}
		return orders;
	}
	
	private StringBuilder getAccessRequest(){
		StringBuilder stb = new StringBuilder();

		stb.append("<?xml version=\"1.0\" ?>\n<AccessRequest>\n<AccessLicenseNumber>5C8687F17E98C558" 
				+ "</AccessLicenseNumber>\n<UserId>ic.tim" 
				+ "</UserId>\n<Password>Mofoit37"
				+ "</Password>\n</AccessRequest>\n<?xml version=\"1.0\" ?>\n");
		
		//logger.debug(stb);
		return stb;
	}

}