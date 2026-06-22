package com.envirowatch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Air quality measurement reading")
public class MeasurementDto {

    @Schema(description = "Internal measurement ID", example = "42")
    private Long id;

    @Schema(description = "Location ID", example = "1")
    private Long locationId;

    @Schema(description = "Location name", example = "Almaty NUR")
    private String locationName;

    @Schema(description = "Parameter ID", example = "1")
    private Long parameterId;

    @Schema(description = "Parameter system name", example = "pm25")
    private String parameterName;

    @Schema(description = "Parameter display name", example = "PM2.5")
    private String parameterDisplayName;

    @Schema(description = "Measured value", example = "45.3")
    private Double value;

    @Schema(description = "Unit of measurement", example = "µg/m³")
    private String unit;

    @Schema(description = "When the measurement was taken")
    private LocalDateTime measuredAt;
}
