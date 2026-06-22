package com.envirowatch.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAqResponseDto<T> {
    private Meta meta;
    private List<T> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private String name;
        private String website;
        private Integer page;
        private Integer limit;
        private Long found;
    }
}
