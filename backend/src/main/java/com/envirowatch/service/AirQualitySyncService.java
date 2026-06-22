package com.envirowatch.service;

import com.envirowatch.client.OpenAqClient;
import com.envirowatch.client.dto.OpenAqLatestDto;
import com.envirowatch.client.dto.OpenAqLocationDto;
import com.envirowatch.client.dto.OpenAqParameterDto;
import com.envirowatch.client.dto.OpenAqResponseDto;
import com.envirowatch.entity.Location;
import com.envirowatch.entity.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AirQualitySyncService {

    private static final Logger log = LoggerFactory.getLogger(AirQualitySyncService.class);
    private static final int LOCATIONS_PER_PAGE = 50;
    private static final int MAX_PAGES = 3;

    private final OpenAqClient openAqClient;
    private final LocationService locationService;
    private final ParameterService parameterService;
    private final MeasurementService measurementService;

    public AirQualitySyncService(OpenAqClient openAqClient,
                                  LocationService locationService,
                                  ParameterService parameterService,
                                  MeasurementService measurementService) {
        this.openAqClient = openAqClient;
        this.locationService = locationService;
        this.parameterService = parameterService;
        this.measurementService = measurementService;
    }

    /**
     * Full sync: fetch locations from OpenAQ, upsert them and their parameters,
     * then fetch and store latest measurements for each location.
     */
    public void syncAll() {
        log.info("Starting air quality data sync...");
        long startTime = System.currentTimeMillis();
        int totalLocations = 0;
        int totalMeasurements = 0;
        int skippedDuplicates = 0;

        try {
            // Phase 1: Fetch and upsert locations + parameters
            Map<Long, Location> syncedLocations = new HashMap<>();

            for (int page = 1; page <= MAX_PAGES; page++) {
                OpenAqResponseDto<OpenAqLocationDto> response = openAqClient
                        .fetchLocations(LOCATIONS_PER_PAGE, page)
                        .block();

                if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                    log.info("No more locations at page {}", page);
                    break;
                }

                for (OpenAqLocationDto locDto : response.getResults()) {
                    try {
                        Location location = locationService.upsertFromOpenAq(locDto);
                        syncedLocations.put(locDto.getId(), location);
                        totalLocations++;

                        // Upsert parameters embedded in location
                        if (locDto.getParameters() != null) {
                            for (OpenAqParameterDto paramDto : locDto.getParameters()) {
                                parameterService.upsertFromOpenAq(paramDto);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Failed to sync location {}: {}", locDto.getId(), e.getMessage());
                    }
                }
            }

            log.info("Synced {} locations. Fetching latest measurements...", totalLocations);

            // Phase 2: Fetch latest measurements for each synced location
            for (Map.Entry<Long, Location> entry : syncedLocations.entrySet()) {
                try {
                    OpenAqResponseDto<OpenAqLatestDto> latestResponse = openAqClient
                            .fetchLatestMeasurements(entry.getKey())
                            .block();

                    if (latestResponse == null || latestResponse.getResults() == null) {
                        continue;
                    }

                    for (OpenAqLatestDto latest : latestResponse.getResults()) {
                        if (latest.getValue() == null || latest.getDatetime() == null
                                || latest.getDatetime().getUtc() == null || latest.getParameter() == null) {
                            continue;
                        }

                        // Find or create the parameter
                        OpenAqParameterDto paramDto = new OpenAqParameterDto();
                        paramDto.setId(latest.getParameter().getId());
                        paramDto.setName(latest.getParameter().getName());
                        paramDto.setDisplayName(latest.getParameter().getDisplayName());
                        paramDto.setUnits(latest.getParameter().getUnits());
                        Parameter parameter = parameterService.upsertFromOpenAq(paramDto);

                        LocalDateTime measuredAt = latest.getDatetime().getUtc().toLocalDateTime();
                        boolean saved = measurementService.saveIfNotDuplicate(
                                entry.getValue(), parameter, latest.getValue(), measuredAt);

                        if (saved) {
                            totalMeasurements++;
                        } else {
                            skippedDuplicates++;
                        }
                    }
                } catch (Exception e) {
                    log.warn("Failed to fetch measurements for location {}: {}",
                            entry.getKey(), e.getMessage());
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("Sync completed in {}ms. Locations: {}, New measurements: {}, Duplicates skipped: {}",
                    duration, totalLocations, totalMeasurements, skippedDuplicates);

        } catch (Exception e) {
            log.error("Sync failed with error", e);
        }
    }
}
