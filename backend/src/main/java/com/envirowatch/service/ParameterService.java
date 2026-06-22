package com.envirowatch.service;

import com.envirowatch.client.dto.OpenAqParameterDto;
import com.envirowatch.dto.ParameterDto;
import com.envirowatch.entity.Parameter;
import com.envirowatch.mapper.ParameterMapper;
import com.envirowatch.repository.ParameterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ParameterService {

    private static final Logger log = LoggerFactory.getLogger(ParameterService.class);

    private final ParameterRepository parameterRepository;
    private final ParameterMapper parameterMapper;

    public ParameterService(ParameterRepository parameterRepository, ParameterMapper parameterMapper) {
        this.parameterRepository = parameterRepository;
        this.parameterMapper = parameterMapper;
    }

    public List<ParameterDto> findAll() {
        return parameterRepository.findAll().stream()
                .map(parameterMapper::toDto)
                .toList();
    }

    /**
     * Upsert a parameter from OpenAQ data. Returns the persisted entity.
     * Uses externalId for deduplication — if exists, updates; otherwise creates.
     */
    @Transactional
    public Parameter upsertFromOpenAq(OpenAqParameterDto dto) {
        return parameterRepository.findByExternalId(dto.getId())
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setDisplayName(dto.getDisplayName());
                    existing.setUnits(dto.getUnits());
                    existing.setDescription(dto.getDescription());
                    return parameterRepository.save(existing);
                })
                .orElseGet(() -> {
                    Parameter newParam = Parameter.builder()
                            .externalId(dto.getId())
                            .name(dto.getName())
                            .displayName(dto.getDisplayName())
                            .units(dto.getUnits())
                            .description(dto.getDescription())
                            .build();
                    log.info("Saving new parameter: {} ({})", dto.getName(), dto.getUnits());
                    return parameterRepository.save(newParam);
                });
    }
}
