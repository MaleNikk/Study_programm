package com.kafka.producer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServerPortConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Value("${spring.serverPortFirst}")
    private Integer serverPort;

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        factory.setPort(serverPort);
    }
}
