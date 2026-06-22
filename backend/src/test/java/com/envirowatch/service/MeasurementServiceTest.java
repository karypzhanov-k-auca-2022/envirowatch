package com.envirowatch.service;

import com.envirowatch.entity.Location;
import com.envirowatch.entity.Measurement;
import com.envirowatch.entity.Parameter;
import com.envirowatch.mapper.MeasurementMapper;
import com.envirowatch.repository.MeasurementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private MeasurementMapper measurementMapper;
    @InjectMocks
    private MeasurementService measurementService;

    @Test
    void saveIfNotDuplicate_newMeasurement_savesAndReturnsTrue() {
        Location location = Location.builder().id(1L).name("Station").country("KZ").build();
        Parameter parameter = Parameter.builder().id(1L).name("pm25").units("µg/m³").build();
        LocalDateTime measuredAt = LocalDateTime.of(2025, 1, 15, 10, 0);

        when(measurementRepository.existsByLocationIdAndParameterIdAndMeasuredAt(1L, 1L, measuredAt))
                .thenReturn(false);
        when(measurementRepository.save(any(Measurement.class)))
                .thenReturn(Measurement.builder().id(1L).build());

        boolean result = measurementService.saveIfNotDuplicate(location, parameter, 45.3, measuredAt);

        assertTrue(result);
        verify(measurementRepository).save(any(Measurement.class));
    }

    @Test
    void saveIfNotDuplicate_duplicateMeasurement_returnsFalse() {
        Location location = Location.builder().id(1L).name("Station").country("KZ").build();
        Parameter parameter = Parameter.builder().id(1L).name("pm25").units("µg/m³").build();
        LocalDateTime measuredAt = LocalDateTime.of(2025, 1, 15, 10, 0);

        when(measurementRepository.existsByLocationIdAndParameterIdAndMeasuredAt(1L, 1L, measuredAt))
                .thenReturn(true);

        boolean result = measurementService.saveIfNotDuplicate(location, parameter, 45.3, measuredAt);

        assertFalse(result);
        verify(measurementRepository, never()).save(any());
    }
}
