package com.github.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper;
    }

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }
}
