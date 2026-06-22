package com.envirowatch.controller;

import com.envirowatch.dto.LocationDto;
import com.envirowatch.dto.MeasurementDto;
import com.envirowatch.service.LocationService;
import com.envirowatch.service.MeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Locations", description = "Air quality monitoring stations")
public class LocationController {

    private final LocationService locationService;
    private final MeasurementService measurementService;

    public LocationController(LocationService locationService, MeasurementService measurementService) {
        this.locationService = locationService;
        this.measurementService = measurementService;
    }

    @GetMapping
    @Operation(summary = "Get all locations", description = "Returns a paginated list of monitoring stations")
    public ResponseEntity<Page<LocationDto>> getAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(locationService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get location by ID", description = "Returns details for a specific monitoring station")
    public ResponseEntity<LocationDto> getById(
            @Parameter(description = "Location ID") @PathVariable Long id) {
        return ResponseEntity.ok(locationService.findById(id));
    }

    @GetMapping("/{id}/measurements")
    @Operation(summary = "Get measurements for a location", description = "Returns paginated measurements for a specific station")
    public ResponseEntity<Page<MeasurementDto>> getMeasurements(
            @Parameter(description = "Location ID") @PathVariable Long id,
            @PageableDefault(size = 50, sort = "measuredAt") Pageable pageable) {
        return ResponseEntity.ok(measurementService.findByLocationId(id, pageable));
    }
}
