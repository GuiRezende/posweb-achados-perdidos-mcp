package com.guilherme.achadosperdidos.reivindicacoes_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class OcorrenciasClientConfig {

    @Value("${services.ocorrencias.url:http://localhost:8082}")
    private String ocorrenciasServiceUrl;

    @Bean
    public RestClient ocorrenciasRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(3));

        return RestClient.builder()
                .baseUrl(ocorrenciasServiceUrl)
                .requestFactory(factory)
                .build();
    }
}