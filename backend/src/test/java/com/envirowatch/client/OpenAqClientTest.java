package com.envirowatch.client;

import com.envirowatch.client.dto.OpenAqLocationDto;
import com.envirowatch.client.dto.OpenAqResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class OpenAqClientTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testParseMockJson() throws Exception {
        // Загружаем mock JSON из ресурсов
        try (InputStream is = getClass().getResourceAsStream("/mock/locations-response.json")) {
            assertNotNull(is, "Mock JSON file not found in resources!");
            
            OpenAqResponseDto<OpenAqLocationDto> response = objectMapper.readValue(is, 
                new TypeReference<OpenAqResponseDto<OpenAqLocationDto>>() {});

            assertNotNull(response, "Parsed response should not be null");
            assertEquals("openaq-api", response.getMeta().getName());
            assertEquals(1234L, response.getMeta().getFound());
            assertEquals(2, response.getResults().size());

            OpenAqLocationDto firstLoc = response.getResults().get(0);
            assertEquals(2178L, firstLoc.getId());
            assertEquals("Example Station Name", firstLoc.getName());
            assertEquals("City Name", firstLoc.getLocality()); // Lombok getter
            assertEquals("US", firstLoc.getCountry().getId());
            assertEquals(34.05, firstLoc.getCoordinates().getLatitude());
            assertTrue(firstLoc.getActive());
            assertEquals(1, firstLoc.getParameters().size());
            assertEquals("pm25", firstLoc.getParameters().get(0).getName());

            System.out.println("=== Mock JSON parsed successfully! ===");
        }
    }

    @Test
    public void testFetchLocationsRealCall() {
        String apiKey = System.getenv("OPENAQ_API_KEY");
        // Пропускаем тест, если ключ API отсутствует в переменных окружения
        Assumptions.assumeTrue(apiKey != null && !apiKey.trim().isEmpty(), 
            "Skipping real API call test: OPENAQ_API_KEY environment variable is not set.");

        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openaq.org")
                .defaultHeader("X-API-Key", apiKey)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "EnviroWatch Integration Test")
                .build();

        OpenAqClient openAqClient = new OpenAqClient(webClient);

        try {
            OpenAqResponseDto<OpenAqLocationDto> response = openAqClient.fetchLocations(2, 1)
                    .block();

            assertNotNull(response, "Response should not be null");
            assertNotNull(response.getResults(), "Results should not be null");
            assertFalse(response.getResults().isEmpty(), "Results should not be empty");

            System.out.println("=== SUCCESSFUL REAL OPENAQ CALL ===");
            System.out.println("Found total: " + response.getMeta().getFound());
            System.out.println("=====================================");

        } catch (Exception e) {
            fail("Integration test failed even with API key: " + e.getMessage());
        }
    }
}
