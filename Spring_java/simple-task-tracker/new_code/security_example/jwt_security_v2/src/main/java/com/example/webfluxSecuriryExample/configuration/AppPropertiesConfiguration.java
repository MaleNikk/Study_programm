package com.example.webfluxSecuriryExample.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppPropertiesConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Value("${spring.serverPort}")
    private Integer serverPort;

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(serverPort);
    }
}
