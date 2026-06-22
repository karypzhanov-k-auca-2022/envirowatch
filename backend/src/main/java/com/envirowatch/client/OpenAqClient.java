package com.envirowatch.client;

import com.envirowatch.client.dto.OpenAqLatestDto;
import com.envirowatch.client.dto.OpenAqLocationDto;
import com.envirowatch.client.dto.OpenAqResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class OpenAqClient {

    private static final Logger log = LoggerFactory.getLogger(OpenAqClient.class);
    private final WebClient openAqWebClient;

    public OpenAqClient(WebClient openAqWebClient) {
        this.openAqWebClient = openAqWebClient;
    }

    /**
     * Fetch paginated locations from OpenAQ v3.
     */
    public Mono<OpenAqResponseDto<OpenAqLocationDto>> fetchLocations(int limit, int page) {
        log.debug("Fetching locations from OpenAQ: limit={}, page={}", limit, page);
        return openAqWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/locations")
                        .queryParam("limit", limit)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<OpenAqResponseDto<OpenAqLocationDto>>() {})
                .doOnError(WebClientResponseException.class,
                        e -> log.error("OpenAQ locations error: {} {}", e.getStatusCode(), e.getMessage()));
    }

    /**
     * Fetch latest measurements for a specific location from OpenAQ v3.
     */
    public Mono<OpenAqResponseDto<OpenAqLatestDto>> fetchLatestMeasurements(Long locationId) {
        log.debug("Fetching latest measurements for location: {}", locationId);
        return openAqWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v3/locations/{locationId}/latest")
                        .build(locationId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<OpenAqResponseDto<OpenAqLatestDto>>() {})
                .doOnError(WebClientResponseException.class,
                        e -> log.error("OpenAQ latest error for location {}: {} {}", locationId, e.getStatusCode(), e.getMessage()));
    }
}
