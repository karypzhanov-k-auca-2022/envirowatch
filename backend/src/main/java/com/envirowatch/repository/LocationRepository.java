package com.envirowatch.repository;

import com.envirowatch.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByExternalId(Long externalId);
}
