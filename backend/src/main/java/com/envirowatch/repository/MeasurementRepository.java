package com.envirowatch.repository;

import com.envirowatch.entity.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    Page<Measurement> findByLocationId(Long locationId, Pageable pageable);

    @Query("SELECT m FROM Measurement m WHERE m.locationId = :locationId " +
           "AND (:parameterId IS NULL OR m.parameter.id = :parameterId) " +
           "AND (:from IS NULL OR m.measuredAt >= :from) " +
           "AND (:to IS NULL OR m.measuredAt <= :to)")
    Page<Measurement> findFiltered(
            @Param("locationId") Long locationId,
            @Param("parameterId") Long parameterId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    @Query("SELECT m FROM Measurement m " +
           "WHERE (:locationId IS NULL OR m.location.id = :locationId) " +
           "AND (:parameterId IS NULL OR m.parameter.id = :parameterId) " +
           "AND (:from IS NULL OR m.measuredAt >= :from) " +
           "AND (:to IS NULL OR m.measuredAt <= :to)")
    Page<Measurement> findAllFiltered(
            @Param("locationId") Long locationId,
            @Param("parameterId") Long parameterId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable);

    /**
     * Get the latest measurement for each location+parameter combination.
     */
    @Query(value = "SELECT DISTINCT ON (m.location_id, m.parameter_id) m.* " +
                   "FROM measurements m " +
                   "ORDER BY m.location_id, m.parameter_id, m.measured_at DESC",
           nativeQuery = true)
    List<Measurement> findLatestPerLocationAndParameter();

    boolean existsByLocationIdAndParameterIdAndMeasuredAt(
            Long locationId, Long parameterId, LocalDateTime measuredAt);
}
