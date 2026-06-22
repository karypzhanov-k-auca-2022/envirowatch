package com.envirowatch.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAqCountryDto {
    private String id;
    private String name;
}
