package com.csatransport;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

public class CSATransportTrackingClient extends TrackingClient {

	private static final String URL = "http://trace.csatransportation.com";
	private static final String USERNAME = "Integrated";
	private static final String PASSWORD = "Reynard1";

	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			WebDriver driver=getWebDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.navigate().to(URL);
			
			//Maximize the browser
			driver.manage().window().maximize();
			
			if(existsElement(driver, "//a[contains(text(),'Login')]")){
				driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
				driver.findElement(By.name("tm4web_usr")).sendKeys(USERNAME);
				driver.findElement(By.name("tm4web_pwd")).sendKeys(PASSWORD);
				driver.findElement(By.id("btn_login")).click();
				driver.navigate().to("https://trace.csatransportation.com/trace/trace.msw");
			}
			
			if(existsElement(driver, "//a[contains(text(),'Trace')]")){
				
				for (TrackOrder trackOrder : orders) {
					if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
						try {
							String status=null;
							driver.findElement(By.xpath("//form[@id='frmTrace']/table[1]/tbody/tr[2]/td[1]/select/option[2]")).click();
							driver.findElement(By.xpath("//form[@id='frmTrace']/table[1]/tbody/tr[2]/td[2]/select/option[2]")).click();
							driver.findElement(By.xpath("//form[@id='frmTrace']/table[1]/tbody/tr[2]/td[3]/input")).sendKeys(trackOrder.getTrackingNumber());
							driver.findElement(By.xpath(".//*[@id='btn_submit']")).click();
							
							if(existsElement(driver ,"//*[@id='trace_grid']/p")) {
								status=driver.findElement(By.xpath(".//*[@id='trace_grid']/p")).getText();
							}else {
								status=driver.findElement(By.xpath("//*[@id='tbl']/tbody/tr/td[2]/p")).getText();
							}
							if(isValidStatus(status)) {
								status=status.replaceFirst("-", "").replaceFirst("  ", "");
								logs.add("Code : "+status);
								logs.add("Description : "+status);
								APIShipmentStatus apiShipmentStatus=new APIShipmentStatus();
								apiShipmentStatus.setCarrierId(trackOrder.getCarrierId());
								apiShipmentStatus.setApiStatusCode(status);
								apiShipmentStatus.setApiStatusDesc(status);
								setCurrentOrderStatus(trackOrder,apiShipmentStatus);
								continue;
							}
						}catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			if(existsElement(driver, "//a[contains(text(),'Logout')]")){
				driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
			}
			//driver.close();
		}
		return orders;
	}

}
