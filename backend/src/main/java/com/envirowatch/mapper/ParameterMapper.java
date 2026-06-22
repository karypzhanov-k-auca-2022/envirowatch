package com.envirowatch.mapper;

import com.envirowatch.dto.ParameterDto;
import com.envirowatch.entity.Parameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    ParameterDto toDto(Parameter entity);
}
