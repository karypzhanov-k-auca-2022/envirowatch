package com.envirowatch.repository;

import com.envirowatch.entity.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {
    Optional<Parameter> findByExternalId(Long externalId);
    Optional<Parameter> findByName(String name);
}
