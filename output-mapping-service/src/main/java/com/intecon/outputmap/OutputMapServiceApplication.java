package com.intecon.outputmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OutputMapServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(OutputMapServiceApplication.class, args);
	}


}