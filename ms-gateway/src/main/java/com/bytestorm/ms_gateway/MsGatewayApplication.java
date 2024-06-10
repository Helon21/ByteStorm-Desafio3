package com.bytestorm.ms_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@SpringBootApplication
@EnableDiscoveryClient
public class MsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGatewayApplication.class, args);
	}

	public RouteLocator rotas(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/propostas/**").uri("lb://ms-propostas"))
				.route(r -> r.path("/funcionarios/**").uri("lb://msfuncionarios"))
				.route(r -> r.path("/resultados/**").uri("lb://msresultados"))
				.build();
	}

}
