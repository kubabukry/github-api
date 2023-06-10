package com.github.api.configuration;

import com.github.api.controller.ExceptionController;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ExceptionController exceptionController() {
        return new ExceptionController();
    }
}
