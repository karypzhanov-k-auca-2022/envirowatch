package com.envirowatch.mapper;

import com.envirowatch.dto.MeasurementDto;
import com.envirowatch.entity.Measurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    @Mapping(source = "parameter.id", target = "parameterId")
    @Mapping(source = "parameter.name", target = "parameterName")
    @Mapping(source = "parameter.displayName", target = "parameterDisplayName")
    @Mapping(source = "parameter.units", target = "unit")
    MeasurementDto toDto(Measurement entity);
}
