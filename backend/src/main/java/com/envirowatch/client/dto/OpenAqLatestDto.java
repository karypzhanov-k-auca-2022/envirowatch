package com.envirowatch.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAqLatestDto {
    private Long sensorsId;
    private Double value;

    @JsonProperty("datetime")
    private DatetimeInfo datetime;

    private OpenAqParameterSummary parameter;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DatetimeInfo {
        private OffsetDateTime utc;
        private OffsetDateTime local;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OpenAqParameterSummary {
        private Long id;
        private String name;
        private String units;
        private String displayName;
    }
}
