package com.envirowatch.service;

import com.envirowatch.client.dto.OpenAqCoordinatesDto;
import com.envirowatch.client.dto.OpenAqCountryDto;
import com.envirowatch.client.dto.OpenAqLocationDto;
import com.envirowatch.dto.LocationDto;
import com.envirowatch.entity.Location;
import com.envirowatch.exception.ResourceNotFoundException;
import com.envirowatch.mapper.LocationMapper;
import com.envirowatch.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;
    @Mock
    private LocationMapper locationMapper;
    @InjectMocks
    private LocationService locationService;

    @Test
    void findAll_returnsPageOfDtos() {
        Location entity = Location.builder().id(1L).name("Station A").country("KZ").build();
        LocationDto dto = LocationDto.builder().id(1L).name("Station A").country("KZ").build();
        Page<Location> page = new PageImpl<>(List.of(entity));

        when(locationRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(locationMapper.toDto(entity)).thenReturn(dto);

        Page<LocationDto> result = locationService.findAll(PageRequest.of(0, 20));

        assertEquals(1, result.getTotalElements());
        assertEquals("Station A", result.getContent().get(0).getName());
    }

    @Test
    void findById_existingId_returnsDto() {
        Location entity = Location.builder().id(1L).name("Station A").country("KZ").build();
        LocationDto dto = LocationDto.builder().id(1L).name("Station A").country("KZ").build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(locationMapper.toDto(entity)).thenReturn(dto);

        LocationDto result = locationService.findById(1L);
        assertEquals("Station A", result.getName());
    }

    @Test
    void findById_nonExistingId_throwsNotFound() {
        when(locationRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> locationService.findById(999L));
    }

    @Test
    void upsertFromOpenAq_newLocation_creates() {
        OpenAqLocationDto dto = buildOpenAqLocationDto(100L, "New Station", "Almaty", "KZ", 43.0, 76.0);
        Location saved = Location.builder().id(1L).externalId(100L).name("New Station").country("KZ").build();

        when(locationRepository.findByExternalId(100L)).thenReturn(Optional.empty());
        when(locationRepository.save(any(Location.class))).thenReturn(saved);

        Location result = locationService.upsertFromOpenAq(dto);

        assertEquals(100L, result.getExternalId());
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void upsertFromOpenAq_existingLocation_updates() {
        OpenAqLocationDto dto = buildOpenAqLocationDto(100L, "Updated Name", "Almaty", "KZ", 43.0, 76.0);
        Location existing = Location.builder().id(1L).externalId(100L).name("Old Name").country("KZ")
                .createdAt(LocalDateTime.now()).build();

        when(locationRepository.findByExternalId(100L)).thenReturn(Optional.of(existing));
        when(locationRepository.save(existing)).thenReturn(existing);

        Location result = locationService.upsertFromOpenAq(dto);

        assertEquals("Updated Name", result.getName());
        verify(locationRepository).save(existing);
    }

    private OpenAqLocationDto buildOpenAqLocationDto(Long id, String name, String city, String countryCode,
                                                      Double lat, Double lon) {
        OpenAqLocationDto dto = new OpenAqLocationDto();
        dto.setId(id);
        dto.setName(name);
        dto.setLocality(city);
        dto.setActive(true);

        OpenAqCountryDto country = new OpenAqCountryDto();
        country.setId(countryCode);
        country.setName(countryCode);
        dto.setCountry(country);

        OpenAqCoordinatesDto coords = new OpenAqCoordinatesDto();
        coords.setLatitude(lat);
        coords.setLongitude(lon);
        dto.setCoordinates(coords);

        return dto;
    }
}
