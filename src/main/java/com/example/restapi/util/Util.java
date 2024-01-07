package com.example.restapi.util;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class Util {

    private final HttpHeaders headers;;

    public Util(HttpHeaders headers) {
        this.headers = headers;
    }

    @Bean
    public HttpHeaders headers() {
        return new HttpHeaders();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpEntity<?> entity() {
        return new HttpEntity<>(headers);
    }
}
