package com.envirowatch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Air quality parameter type")
public class ParameterDto {

    @Schema(description = "Internal parameter ID", example = "1")
    private Long id;

    @Schema(description = "External ID from OpenAQ", example = "2")
    private Long externalId;

    @Schema(description = "System name of the parameter", example = "pm25")
    private String name;

    @Schema(description = "Human-readable display name", example = "PM2.5")
    private String displayName;

    @Schema(description = "Unit of measurement", example = "µg/m³")
    private String units;

    @Schema(description = "Parameter description", example = "Fine particulate matter")
    private String description;
}
