package com.soluship.tracking.service;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.OrderStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.mapper.DBMapper;
import com.soluship.tracking.util.SolushipUtil;

public abstract class TrackingClient {
	
	private static final Logger logger = LoggerFactory.getLogger(TrackingClient.class);
	
	protected List<String> logs=new ArrayList<String>();
	protected List<String> errorMessages=new ArrayList<String>();

	protected DBMapper dbMapper;
	
    public void printConsolidatedLog(){
		if(logs!=null && !logs.isEmpty()){
			for(String message:logs){
				logger.debug(message);
			}
		}
	}
    
    public String getErrorMessage(){
		String errorMessage=null;
		if(errorMessages!=null && !errorMessages.isEmpty()){
			int i=1;
			for(String error:errorMessages){
				if(errorMessage==null){
					errorMessage=new String();
				}
				errorMessage=errorMessage.concat(i+")").concat(error);
				i++;
			}
		}
		if(errorMessage==null){
			errorMessage="Unable to track your order at this time.Please try later.";
		}
		return errorMessage;
	}
	
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public abstract List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders);
	
	public boolean existsElement(WebDriver driver ,String id) {
		try {
			driver.findElement(By.xpath(id));
			return true;
		} catch (Exception e) {
			//Handled exception
		}
		return false;
	}
	
	protected void setCurrentOrderStatus(TrackOrder trackOrder,APIShipmentStatus apiShipmentStatus) {
		List<APIShipmentStatus> apiShipmentStatuses=dbMapper.selectAPIShipmentStatusesByCarrierId(trackOrder.getCarrierId());
		if(apiShipmentStatus!=null) {
			for(APIShipmentStatus dbAPIShipmentStatus:apiShipmentStatuses){
				if(dbAPIShipmentStatus!=null && dbAPIShipmentStatus.getOrderStatusId()>0){
					if(SolushipUtil.isBeginWith(apiShipmentStatus.getApiStatusCode(), dbAPIShipmentStatus.getApiStatusCode())){
						OrderStatus orderStatus=dbMapper.selectOrderStatusById(dbAPIShipmentStatus.getOrderStatusId());
						if(orderStatus!=null) {
							trackOrder.setCurrentOrderStatus(orderStatus);
						}
						break;
					}
				}	
			}
		}
	}
	
	public void configureInstance(DBMapper dbMapper) {
		this.dbMapper=dbMapper;		
		logs.clear();
		errorMessages.clear();
	}

	public static WebDriver getWebDriver(){
		System.setProperty("webdriver.gecko.driver","/home/sundar/software/geckodriver-v0.24.0-linux64/geckodriver");
		return new FirefoxDriver();
		//return new HtmlUnitDriver(true);
	}
	
	public static boolean isValidStatus(String status) {
		if(status!=null && !status.isEmpty() && (!status.equalsIgnoreCase("Invalid Shipper number") && !status.equalsIgnoreCase("Not authorized to account")
				&& !status.equalsIgnoreCase("Invalid pro number") && !status.equalsIgnoreCase("Invalid PO number ") 
				&& !status.equalsIgnoreCase("Invalid BOL number"))) {
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		WebDriver driver = getWebDriver();
		driver.get("http://www.google.com");
		driver.close();
	}
	
}
