package com.envirowatch.mapper;

import com.envirowatch.dto.LocationDto;
import com.envirowatch.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto toDto(Location entity);
}
