package com.github.api.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
@Bean
public WebClient webClient(){
    return WebClient.builder()
            .baseUrl("https://api.github.com")
            .filter((request, next) -> next.exchange(request)
                    .flatMap(response -> {
                        if (response.statusCode().equals(HttpStatus.NOT_ACCEPTABLE)) {
                            // Handle NOT_ACCEPTABLE status code
                            // You can log the error or perform custom actions here
                            return Mono.just(response);
                        } else {
                            // Continue with default error handling for other status codes
                            return next.exchange(request);
                        }
                    }))
            .build();
}
}
