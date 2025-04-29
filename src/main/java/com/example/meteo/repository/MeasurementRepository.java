package com.example.meteo.repository;

import com.example.meteo.entity.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource(collectionResourceRel="Measurement", path="measurement")
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    // Find measurements by city name and country code
    @Query("SELECT m FROM Measurement m " +
            "JOIN m.city c " +
            "JOIN c.country co " +
            "WHERE c.name = :cityName AND co.code = :countryCode")
    List<Measurement> findByCityNameAndCountryCode(
            @Param("cityName") String cityName,
            @Param("countryCode") String countryCode);

    // Find measurements by city ID
    @Query("SELECT m FROM Measurement m WHERE m.city.id = :cityId")
    List<Measurement> findByCityId(@Param("cityId") Long cityId);

    // Find measurement by city name, country code and exact timestamp
    @Query("SELECT m FROM Measurement m " +
            "JOIN m.city c " +
            "JOIN c.country co " +
            "WHERE c.name = :cityName " +
            "AND co.code = :countryCode " +
            "AND m.timestamp = :timestamp")
    Optional<Measurement> findByCityNameAndCountryCodeAndTimestamp(
            @Param("cityName") String cityName,
            @Param("countryCode") String countryCode,
            @Param("timestamp") LocalDateTime timestamp);

    // Additional useful queries
    List<Measurement> findByCityIdOrderByTimestampDesc(Long cityId);

    @Query("SELECT m FROM Measurement m " +
            "WHERE m.city.id = :cityId " +
            "AND m.timestamp BETWEEN :start AND :end " +
            "ORDER BY m.timestamp DESC")
    List<Measurement> findByCityAndDateRange(
            @Param("cityId") Long cityId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

        Optional<Measurement> findFirstByCityIdOrderByTimestampDesc(Long cityId);

    List<Measurement> findByCityIdAndTimestampAfter(Long cityId, Instant since);

}