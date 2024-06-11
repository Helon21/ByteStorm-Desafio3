package com.bytestorm.ms_propostas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class MsPropostasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPropostasApplication.class, args);
	}

}
