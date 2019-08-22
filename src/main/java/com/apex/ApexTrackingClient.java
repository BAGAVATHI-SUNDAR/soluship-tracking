package com.apex;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.TrackOrder;
import com.soluship.tracking.service.TrackingClient;

public class ApexTrackingClient extends TrackingClient {

	private static final String URL = "https://www.apexltl.com/?p=10";
	private static final String USERNAME = "ryan.blakey@integratedcarriers.com";
	private static final String PASSWORD = "zpfvqr";

	@Override
	public List<TrackOrder> trackCurrentOrderStatus(List<TrackOrder> orders) {
		if(orders!=null && !orders.isEmpty()) {
			WebDriver driver=getWebDriver();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.navigate().to(URL);
			
			//Maximize the browser
			driver.manage().window().maximize();
			
			if(existsElement(driver, "//*[@id='loginFormButton']")){
				driver.findElement(By.id("usernameHeader")).sendKeys(USERNAME);
				driver.findElement(By.id("passwordHeader")).sendKeys(PASSWORD);
				driver.findElement(By.id("loginFormButton")).click();
			}
			
			for (TrackOrder trackOrder : orders) {
				if(trackOrder.getTrackingNumber()!=null && !trackOrder.getTrackingNumber().isEmpty()) {
					try {
						//Pro Bill Search
						driver.findElement(By.name("Pro1")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.name("submitSearch")).click();
						if(existsElement(driver, "//*[@id='results']/tbody")){
							List<WebElement> webElements = driver.findElements(By.xpath("//*[@id='results']/tbody"));
							if(webElements!=null && !webElements.isEmpty()) {
								boolean isProMatched=false;
								String status=null;
								for(WebElement webElement:webElements) {
									String proNumber=webElement.findElement(By.xpath("//td[1]")).getText();
									if(proNumber!=null && !proNumber.isEmpty() && proNumber.equalsIgnoreCase(trackOrder.getTrackingNumber())) {
										isProMatched=true;
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(!isProMatched) {
									for(WebElement webElement:webElements) {
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(isValidStatus(status)) {
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
						}
						
						//BOL Search
						driver.findElement(By.name("Pro1")).clear();
						driver.findElement(By.xpath("//*[@id='content']/form/table/tbody/tr/td/table[1]/tbody/tr/td[2]/input")).click();
						driver.findElement(By.name("Pro1")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.name("submitSearch")).click();
						if(existsElement(driver, "//*[@id='results']/tbody")){
							List<WebElement> webElements = driver.findElements(By.xpath("//*[@id='results']/tbody"));
							if(webElements!=null && !webElements.isEmpty()) {
								boolean isProMatched=false;
								String status=null;
								for(WebElement webElement:webElements) {
									String proNumber=webElement.findElement(By.xpath("//td[1]")).getText();
									if(proNumber!=null && !proNumber.isEmpty() && proNumber.equalsIgnoreCase(trackOrder.getTrackingNumber())) {
										isProMatched=true;
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(!isProMatched) {
									for(WebElement webElement:webElements) {
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(isValidStatus(status)) {
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
						}
						
						//Purchase Order Search
						driver.findElement(By.name("Pro1")).clear();
						driver.findElement(By.xpath("//*[@id='content']/form/table/tbody/tr/td/table[1]/tbody/tr/td[3]/input")).click();
						driver.findElement(By.name("Pro1")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.name("submitSearch")).click();
						if(existsElement(driver, "//*[@id='results']/tbody")){
							List<WebElement> webElements = driver.findElements(By.xpath("//*[@id='results']/tbody"));
							if(webElements!=null && !webElements.isEmpty()) {
								boolean isProMatched=false;
								String status=null;
								for(WebElement webElement:webElements) {
									String proNumber=webElement.findElement(By.xpath("//td[1]")).getText();
									if(proNumber!=null && !proNumber.isEmpty() && proNumber.equalsIgnoreCase(trackOrder.getTrackingNumber())) {
										isProMatched=true;
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(!isProMatched) {
									for(WebElement webElement:webElements) {
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(isValidStatus(status)) {
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
						}
						
						//Shipper Number Search
						driver.findElement(By.name("Pro1")).clear();
						driver.findElement(By.xpath("//*[@id='content']/form/table/tbody/tr/td/table[1]/tbody/tr/td[3]/input")).click();
						driver.findElement(By.name("Pro1")).sendKeys(trackOrder.getTrackingNumber());
						driver.findElement(By.name("submitSearch")).click();
						if(existsElement(driver, "//*[@id='results']/tbody")){
							List<WebElement> webElements = driver.findElements(By.xpath("//*[@id='results']/tbody"));
							if(webElements!=null && !webElements.isEmpty()) {
								boolean isProMatched=false;
								String status=null;
								for(WebElement webElement:webElements) {
									String proNumber=webElement.findElement(By.xpath("//td[1]")).getText();
									if(proNumber!=null && !proNumber.isEmpty() && proNumber.equalsIgnoreCase(trackOrder.getTrackingNumber())) {
										isProMatched=true;
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(!isProMatched) {
									for(WebElement webElement:webElements) {
										status=webElement.findElement(By.xpath("//td[2]")).getText();
										if(isValidStatus(status)) {
											break;
										}
									}
								}
								if(isValidStatus(status)) {
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
						}
						
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(existsElement(driver, "//*[@id='logoutFormButton']")){
				driver.findElement(By.id("logoutFormButton")).click();
			}
			driver.close();
		}
		return orders;
	}

}
