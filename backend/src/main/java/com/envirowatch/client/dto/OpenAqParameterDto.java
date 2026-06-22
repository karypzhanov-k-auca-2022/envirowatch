package com.envirowatch.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAqParameterDto {
    private Long id;
    private String name;
    private String displayName;
    private String units;
    private String description;
}
