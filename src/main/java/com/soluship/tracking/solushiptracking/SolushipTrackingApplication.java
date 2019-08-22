package com.soluship.tracking.solushiptracking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.soluship.tracking.mapper")
@ComponentScan(basePackages= {"com.soluship.tracking.controller"})
@SpringBootApplication
public class SolushipTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolushipTrackingApplication.class, args);
	}

}
