package com.intecon.documentreading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.intecon.documentreading.config.AxonConfig;

@SpringBootApplication
@EnableDiscoveryClient
@Import({AxonConfig.class })
public class MongoDbApp1Application {
	
	//@Autowired
	//private DocumentHeaderRepository documentHeaderRepository;
	
	//@Autowired
	//private DocumentDetailRepository documentDetailRepository;
	public static void main(String[] args) {
		System.out.println("Degisiklik");
		SpringApplication.run(MongoDbApp1Application.class, args);
	}
}
