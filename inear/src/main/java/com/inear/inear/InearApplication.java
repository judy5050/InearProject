package com.inear.inear;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class InearApplication {

	public static void main(String[] args) {
		SpringApplication.run(InearApplication.class, args);
	}

}
