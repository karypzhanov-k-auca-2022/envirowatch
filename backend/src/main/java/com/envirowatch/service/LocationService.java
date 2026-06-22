package com.envirowatch.service;

import com.envirowatch.client.dto.OpenAqLocationDto;
import com.envirowatch.dto.LocationDto;
import com.envirowatch.entity.Location;
import com.envirowatch.exception.ResourceNotFoundException;
import com.envirowatch.mapper.LocationMapper;
import com.envirowatch.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Transactional(readOnly = true)
    public Page<LocationDto> findAll(Pageable pageable) {
        return locationRepository.findAll(pageable).map(locationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public LocationDto findById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with id: " + id));
        return locationMapper.toDto(location);
    }

    /**
     * Upsert a location from OpenAQ data. Returns the persisted entity.
     * Uses externalId for deduplication.
     */
    @Transactional
    public Location upsertFromOpenAq(OpenAqLocationDto dto) {
        String countryCode = dto.getCountry() != null ? dto.getCountry().getId() : "XX";
        Double lat = dto.getCoordinates() != null ? dto.getCoordinates().getLatitude() : null;
        Double lon = dto.getCoordinates() != null ? dto.getCoordinates().getLongitude() : null;

        return locationRepository.findByExternalId(dto.getId())
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setCity(dto.getLocality());
                    existing.setCountry(countryCode);
                    existing.setLatitude(lat);
                    existing.setLongitude(lon);
                    existing.setIsActive(dto.getActive() != null ? dto.getActive() : true);
                    return locationRepository.save(existing);
                })
                .orElseGet(() -> {
                    Location newLocation = Location.builder()
                            .externalId(dto.getId())
                            .name(dto.getName())
                            .city(dto.getLocality())
                            .country(countryCode)
                            .latitude(lat)
                            .longitude(lon)
                            .isActive(dto.getActive() != null ? dto.getActive() : true)
                            .build();
                    log.info("Saving new location: {} ({})", dto.getName(), countryCode);
                    return locationRepository.save(newLocation);
                });
    }
}
