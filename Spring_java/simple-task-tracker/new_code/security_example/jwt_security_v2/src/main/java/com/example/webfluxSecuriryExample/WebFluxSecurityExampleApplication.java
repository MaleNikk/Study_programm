package com.example.webfluxSecuriryExample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example")
public class WebFluxSecurityExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxSecurityExampleApplication.class, args);
	}

}
