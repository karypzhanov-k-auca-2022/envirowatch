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
@Schema(description = "Air quality monitoring location")
public class LocationDto {

    @Schema(description = "Internal location ID", example = "1")
    private Long id;

    @Schema(description = "External ID from OpenAQ", example = "2178")
    private Long externalId;

    @Schema(description = "Station name", example = "Almaty NUR")
    private String name;

    @Schema(description = "City where the station is located", example = "Almaty")
    private String city;

    @Schema(description = "Country code (ISO 3166-1 alpha-2)", example = "KZ")
    private String country;

    @Schema(description = "Latitude", example = "43.238949")
    private Double latitude;

    @Schema(description = "Longitude", example = "76.945669")
    private Double longitude;

    @Schema(description = "Whether the station is currently active", example = "true")
    private Boolean isActive;

    @Schema(description = "Record creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp")
    private LocalDateTime updatedAt;
}
