package com.thompsonfreight;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

public class ThomsanFreightTrackingClient extends TrackingClient {

	private static final String URL = null;
	private static final String USERNAME = null;
	private static final String PASSWORD = null;

	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.navigate().to(URL);
			
			//Maximize the browser
			driver.manage().window().maximize();
			
			driver.findElement(By.id("usernameHeader")).sendKeys(USERNAME);
			driver.findElement(By.id("passwordHeader")).sendKeys(PASSWORD);
			driver.findElement(By.id("loginFormButton")).click();
			
			for (TrackOrder trackOrder : orders) {
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
					try {}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			driver.findElement(By.xpath("//a[contains(text(),'Log Out')]")).click();
		}
		return orders;
	}

}
