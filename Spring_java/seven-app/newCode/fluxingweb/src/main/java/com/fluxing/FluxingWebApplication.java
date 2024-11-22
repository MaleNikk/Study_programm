package com.fluxing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fluxing")
public class FluxingWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(FluxingWebApplication.class, args);
	}
}
