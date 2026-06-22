package com.envirowatch.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAqLocationDto {
    private Long id;
    private String name;
    private String locality;
    private OpenAqCountryDto country;
    private OpenAqCoordinatesDto coordinates;
    private Boolean active;
    private List<OpenAqParameterDto> parameters;
}
