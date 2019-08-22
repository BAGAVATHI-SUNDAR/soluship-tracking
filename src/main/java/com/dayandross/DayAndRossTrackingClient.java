package com.dayandross;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

public class DayAndRossTrackingClient extends TrackingClient{
	
	private static final String URL = "http://www.dayross.ca/Forms/TrackShipmentResult.aspx?shipmentnumber=*trackingnum&multirec=Y&langid=en&frompage=TS";
	private static final String USERNAME = "ryan.blakey@integratedcarriers.com";
	private static final String PASSWORD = "Reynardblakey1#";

	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			WebDriver driver=getWebDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.navigate().to(URL);
			
			//Maximize the browser
			driver.manage().window().maximize();
			
			if(existsElement(driver, "//a[contains(text(),'Log in')]")){
				driver.findElement(By.xpath("//a[contains(text(),'Log in')]")).click();
				driver.findElement(By.id("edit-name")).sendKeys(USERNAME);
				driver.findElement(By.id("edit-pass")).sendKeys(PASSWORD);
				driver.findElement(By.id("edit-submit")).click();
				driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[1]/div[3]/div/div/div[3]/ul/li[2]/a")).click();
			}
			
			for (TrackOrder trackOrder : orders) {
				driver.findElement(By.id("probill")).clear();
				driver.findElement(By.id("reference")).clear();
				driver.findElement(By.id("order")).clear();
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
					try {
						driver.findElement(By.id("probill")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.id("probill_search")).click();
							   
						if(existsElement(driver, "//form[@id='track_form']/div/div/table[2]")){
							String status=driver.findElement(By.xpath("//form[@id='track_form']/div/div/table[2]/tbody/tr/td[4]")).getText();
							if(status!=null && !status.isEmpty()) {
								logs.add("Code : "+status);
								logs.add("Description : "+status);
								APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
								apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
								apiShipmentStatus.setApiStatusCode(status);
								apiShipmentStatus.setApiStatusDesc(status);
								setCurrentOrderStatus(trackOrder,apiShipmentStatus);
								continue;
							}
						}
						
						driver.findElement(By.id("probill")).clear();
						driver.findElement(By.id("reference")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.id("reference_search")).click();
						if(existsElement(driver, "//form[@id='track_form']/div/div/table[2]")){
							String status=driver.findElement(By.xpath("//form[@id='track_form']/div/div/table[2]/tbody/tr/td[4]")).getText();
							if(status!=null && !status.isEmpty()) {
								logs.add("Code : "+status);
								logs.add("Description : "+status);
								APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
								apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
								apiShipmentStatus.setApiStatusCode(status);
								apiShipmentStatus.setApiStatusDesc(status);
								setCurrentOrderStatus(trackOrder,apiShipmentStatus);
								continue;
							}
						}
						
						driver.findElement(By.id("probill")).clear();
						driver.findElement(By.id("reference")).clear();
						driver.findElement(By.id("order")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.id("order_search")).click();
						if(existsElement(driver, "//form[@id='track_form']/div/div/table[2]")){
							String status=driver.findElement(By.xpath("//form[@id='track_form']/div/div/table[2]/tbody/tr/td[4]")).getText();
							if(status!=null && !status.isEmpty()) {
								logs.add("Code : "+status);
								logs.add("Description : "+status);
								APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
								apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
								apiShipmentStatus.setApiStatusCode(status);
								apiShipmentStatus.setApiStatusDesc(status);
								setCurrentOrderStatus(trackOrder,apiShipmentStatus);
								continue;
							}
						}
						
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			if(existsElement(driver, "//a[contains(text(),'Log Out')]")){
				driver.findElement(By.xpath("//a[contains(text(),'Log Out')]")).click();
			}
			driver.close();
		}
		return orders;
	}

}
