package com.envirowatch.controller;

import com.envirowatch.dto.ParameterDto;
import com.envirowatch.service.ParameterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/parameters")
@Tag(name = "Parameters", description = "Air quality parameter types (PM2.5, O3, NO2, etc.)")
public class ParameterController {

    private final ParameterService parameterService;

    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @GetMapping
    @Operation(summary = "Get all parameters",
               description = "Returns all air quality parameter types available in the system")
    public ResponseEntity<List<ParameterDto>> getAll() {
        return ResponseEntity.ok(parameterService.findAll());
    }
}
