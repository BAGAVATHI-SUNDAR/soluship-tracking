package com.dhl;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.dhl.req.KnownTrackingRequest;
import com.dhl.req.LevelOfDetails;
import com.dhl.req.Request;
import com.dhl.req.ServiceHeader;
import com.dhl.res.ServiceEvent;
import com.dhl.res.ShipmentEvent;
import com.dhl.res.ShipmentInfo;
import com.dhl.res.TrackingResponse;
import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

public class DHLTrackingClient extends TrackingClient{
	
	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			for (TrackOrder trackOrder : orders) {
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
					try {
						KnownTrackingRequest request=new KnownTrackingRequest();
						Request req = new Request();
						ServiceHeader header = new ServiceHeader();
						header.setMessageTime(getXmlGCalendar());
						header.setMessageReference(getUniqueID());
						header.setSiteID("IntCarriers");
						header.setPassword("qplMNV65as");
						req.setServiceHeader(header);
						request.setRequest(req);
						request.setLanguageCode("en");
						request.setLevelOfDetails(LevelOfDetails.LAST_CHECK_POINT_ONLY);
						request.setPiecesEnabled("S");
						request.getAWBNumber().add(trackOrder.getTrackingNumber());
						
						String schemaLoc = "http://www.dhl.com ship-val-req.xsd";
						JAXBContext context = JAXBContext.newInstance(Request.class.getPackage().getName());
						Marshaller marshaller = context.createMarshaller();
						marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
						marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, schemaLoc);
						marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapperImpl());
						StringWriter stringWriter=new StringWriter();
						marshaller.marshal( request, stringWriter );
						String sReq = stringWriter.toString();
						if (sReq != null) {
							URL servletURL = new URL("https://xmlpi-ea.dhl.com/XMLShippingServlet");

							HttpURLConnection servletConnection = null;
							servletConnection = (HttpURLConnection) servletURL.openConnection();
							servletConnection.setDoOutput(true); // to allow

							servletConnection.setDoInput(true);
							servletConnection.setUseCaches(false);
							servletConnection.setRequestMethod("POST"); 
							servletConnection.setReadTimeout(10*1000);

							servletConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
							String len = Integer.toString(sReq.getBytes().length);
							servletConnection.setRequestProperty("Content-Length", len);

							servletConnection.connect();
							OutputStreamWriter wr = new OutputStreamWriter(servletConnection.getOutputStream());
							wr.write(sReq);
							wr.flush();
							wr.close();

							InputStream inputStream = null;
							inputStream = servletConnection.getInputStream();
							StringBuffer response = new StringBuffer();
							int printResponse;

							// Reading the response into StringBuffer
							while ((printResponse = inputStream.read()) != -1) {
								response.append((char) printResponse);
							}
							inputStream.close();

							String sResponse = response.toString();
							if (sResponse != null) {
								TrackingResponse trackingResponse=null;
								if (sResponse != null && !sResponse.trim().isEmpty()) {
									JAXBContext context1 = JAXBContext.newInstance(TrackingResponse.class.getPackage().getName());
									Unmarshaller unmarshaller = context1.createUnmarshaller();
									trackingResponse = (TrackingResponse) unmarshaller.unmarshal(new StringReader( sResponse ) );
									if(trackingResponse!=null && (trackingResponse.getAWBInfo()!=null && trackingResponse.getAWBInfo().size()>0)){
										ShipmentInfo shipmentInfo=trackingResponse.getAWBInfo().get(0).getShipmentInfo();
										if(shipmentInfo!=null && shipmentInfo.getShipmentEvent()!=null && !shipmentInfo.getShipmentEvent().isEmpty()) {
											ShipmentEvent shipmentEvent=shipmentInfo.getShipmentEvent().get(0);
											ServiceEvent serviceEvent=shipmentEvent.getServiceEvent();
											if(serviceEvent!=null && serviceEvent.getEventCode()!=null && !serviceEvent.getEventCode().isEmpty()) {
												logs.add("Code : "+serviceEvent.getEventCode());
												logs.add("Description : "+serviceEvent.getDescription());
												APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
		    	    							apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
		    	    							apiShipmentStatus.setApiStatusCode(serviceEvent.getEventCode());
		    	    							apiShipmentStatus.setApiStatusDesc(serviceEvent.getDescription());
		    	    							setCurrentOrderStatus(trackOrder,apiShipmentStatus);
											}
										}
									}
								}
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				
				}	
			}
		}
		return orders;
	}
	
	class MyNamespacePrefixMapperImpl extends NamespacePrefixMapper {
		public String getPreferredPrefix(String namespaceUri,
				String suggestion, boolean requirePrefix) {
			if ("xsd".equals(namespaceUri)) {
				return "";
			}
			return "req";
		}
	}		

	public static XMLGregorianCalendar getXmlGCalendar() {
		try {
		    return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
		} catch (DatatypeConfigurationException e) {
		    throw new Error(e);
		}
	}
	private static String getUniqueID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
