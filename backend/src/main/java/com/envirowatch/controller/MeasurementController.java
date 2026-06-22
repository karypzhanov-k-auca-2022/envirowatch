package com.envirowatch.controller;

import com.envirowatch.dto.MeasurementDto;
import com.envirowatch.service.MeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/measurements")
@Tag(name = "Measurements", description = "Air quality measurement readings")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    @Operation(summary = "Get all measurements",
               description = "Returns paginated measurements with optional filters")
    public ResponseEntity<Page<MeasurementDto>> getAll(
            @Parameter(description = "Filter by location ID")
            @RequestParam(required = false) Long locationId,

            @Parameter(description = "Filter by parameter ID")
            @RequestParam(required = false) Long parameterId,

            @Parameter(description = "Filter from date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,

            @Parameter(description = "Filter to date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to,

            @PageableDefault(size = 50, sort = "measuredAt") Pageable pageable) {
        return ResponseEntity.ok(measurementService.findAll(locationId, parameterId, from, to, pageable));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest measurements",
               description = "Returns the most recent measurement per location and parameter")
    public ResponseEntity<List<MeasurementDto>> getLatest() {
        return ResponseEntity.ok(measurementService.findLatest());
    }
}
