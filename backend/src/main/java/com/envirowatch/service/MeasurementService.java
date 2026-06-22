package com.envirowatch.service;

import com.envirowatch.dto.MeasurementDto;
import com.envirowatch.entity.Location;
import com.envirowatch.entity.Measurement;
import com.envirowatch.entity.Parameter;
import com.envirowatch.mapper.MeasurementMapper;
import com.envirowatch.repository.MeasurementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementService {

    private static final Logger log = LoggerFactory.getLogger(MeasurementService.class);

    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;

    public MeasurementService(MeasurementRepository measurementRepository, MeasurementMapper measurementMapper) {
        this.measurementRepository = measurementRepository;
        this.measurementMapper = measurementMapper;
    }

    @Transactional(readOnly = true)
    public Page<MeasurementDto> findAll(Long locationId, Long parameterId,
                                         LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return measurementRepository.findAllFiltered(locationId, parameterId, from, to, pageable)
                .map(measurementMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<MeasurementDto> findByLocationId(Long locationId, Pageable pageable) {
        return measurementRepository.findByLocationId(locationId, pageable)
                .map(measurementMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<MeasurementDto> findLatest() {
        return measurementRepository.findLatestPerLocationAndParameter().stream()
                .map(measurementMapper::toDto)
                .toList();
    }

    /**
     * Save a measurement if it doesn't already exist (deduplication by location+parameter+time).
     * Returns true if saved, false if duplicate.
     */
    @Transactional
    public boolean saveIfNotDuplicate(Location location, Parameter parameter, Double value, LocalDateTime measuredAt) {
        if (measurementRepository.existsByLocationIdAndParameterIdAndMeasuredAt(
                location.getId(), parameter.getId(), measuredAt)) {
            return false;
        }
        Measurement measurement = Measurement.builder()
                .location(location)
                .parameter(parameter)
                .value(value)
                .measuredAt(measuredAt)
                .build();
        measurementRepository.save(measurement);
        log.debug("Saved measurement: location={}, param={}, value={}, at={}",
                location.getName(), parameter.getName(), value, measuredAt);
        return true;
    }
}
