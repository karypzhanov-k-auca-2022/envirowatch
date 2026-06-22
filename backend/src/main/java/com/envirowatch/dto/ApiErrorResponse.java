package com.envirowatch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response")
public class ApiErrorResponse {

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error type", example = "NOT_FOUND")
    private String error;

    @Schema(description = "Error message", example = "Location not found with id: 999")
    private String message;

    @Schema(description = "Request path", example = "/api/v1/locations/999")
    private String path;

    @Schema(description = "Timestamp of the error")
    private LocalDateTime timestamp;

    @Schema(description = "Validation error details (if applicable)")
    private List<String> details;
}
