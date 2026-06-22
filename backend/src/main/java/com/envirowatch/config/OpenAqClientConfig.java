package com.envirowatch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAqClientConfig {

    @Value("${openaq.base-url}")
    private String baseUrl;

    @Value("${openaq.api-key:}")
    private String apiKey;

    @Bean
    public WebClient openAqWebClient(WebClient.Builder webClientBuilder) {
        WebClient.Builder builder = webClientBuilder.baseUrl(baseUrl);
        
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            builder.defaultHeader("X-API-Key", apiKey);
        }
        
        builder.defaultHeader("Accept", "application/json");
        builder.defaultHeader("User-Agent", "EnviroWatch Air Quality Platform");

        return builder.build();
    }
}
