package com.envirowatch.controller;

import com.envirowatch.dto.LocationDto;
import com.envirowatch.dto.MeasurementDto;
import com.envirowatch.exception.GlobalExceptionHandler;
import com.envirowatch.exception.ResourceNotFoundException;
import com.envirowatch.service.LocationService;
import com.envirowatch.service.MeasurementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LocationService locationService;

    @MockitoBean
    private MeasurementService measurementService;

    @Test
    void getAll_returnsPageOfLocations() throws Exception {
        LocationDto dto = LocationDto.builder().id(1L).name("Station A").country("KZ").build();
        Page<LocationDto> page = new PageImpl<>(List.of(dto));
        when(locationService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Station A"))
                .andExpect(jsonPath("$.content[0].country").value("KZ"));
    }

    @Test
    void getById_existingId_returnsLocation() throws Exception {
        LocationDto dto = LocationDto.builder().id(1L).name("Station A").country("KZ").build();
        when(locationService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/locations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Station A"));
    }

    @Test
    void getById_nonExistingId_returns404() throws Exception {
        when(locationService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Location not found with id: 999"));

        mockMvc.perform(get("/api/v1/locations/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Location not found with id: 999"));
    }

    @Test
    void getMeasurements_returnsPageOfMeasurements() throws Exception {
        MeasurementDto dto = MeasurementDto.builder()
                .id(1L).locationId(1L).locationName("Station A")
                .parameterId(1L).parameterName("pm25").value(45.0).build();
        Page<MeasurementDto> page = new PageImpl<>(List.of(dto));
        when(measurementService.findByLocationId(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/locations/1/measurements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].parameterName").value("pm25"));
    }
}
