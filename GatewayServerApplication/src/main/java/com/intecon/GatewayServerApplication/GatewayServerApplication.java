package com.intecon.GatewayServerApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableHystrix
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}
//	@Bean
//	 public RouteLocator customRouteLocator(RouteLocatorBuilder builder)  {
//	 return builder.routes()
//	  .route("path_route", r -> r.path("/product").and().method("POST", "PUT", "DELETE").and().host("localhost")
//	  .uri("http://localhost:8082"))
//	  .route("path_route", r -> r.path("/product/**").and().method("GET")
//	  .uri("http://localhost:8081"))
//	  .build();
//	 }

}
