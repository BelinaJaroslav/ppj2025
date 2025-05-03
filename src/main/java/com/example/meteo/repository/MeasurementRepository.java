package com.example.meteo.repository;

import com.example.meteo.entity.Measurement;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource(collectionResourceRel="Measurement", path="measurement")
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    @Query("SELECT m FROM Measurement m JOIN FETCH m.city c JOIN FETCH c.country co " +
            "WHERE c.name = :cityName AND co.code = :countryCode")
    List<Measurement> findByCityNameAndCountryCode(
            @Param("cityName") String cityName,
            @Param("countryCode") String countryCode);

    @Query("SELECT m FROM Measurement m JOIN FETCH m.city WHERE m.city.idCity = :cityId")
    List<Measurement> findByCityId(@Param("cityId") Long cityId);

    @Query("SELECT m FROM Measurement m JOIN FETCH m.city c JOIN FETCH c.country co " +
            "WHERE c.name = :cityName AND co.code = :countryCode " +
            "AND (:timestamp IS NULL OR m.timestamp = :timestamp)")
    Optional<Measurement> findByCityNameAndCountryCodeAndTimestamp(
            @Param("cityName") String cityName,
            @Param("countryCode") String countryCode,
            @Param("timestamp") @Nullable LocalDateTime timestamp);

    @RestResource(exported = false)
    List<Measurement> findByCity_IdCityOrderByTimestampDesc(Long cityId);

    @Query("SELECT m FROM Measurement m JOIN FETCH m.city " +
            "WHERE m.city.idCity = :cityId " +
            "AND m.timestamp BETWEEN :start AND :end " +
            "ORDER BY m.timestamp DESC")
    List<Measurement> findByCityAndDateRange(
            @Param("cityId") Long cityId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    List<Measurement> findByCity_IdCityAndTimestampAfter(Long idCity, Instant timestamp);

    @Query("SELECT AVG(m.temperature) FROM Measurement m WHERE m.city.idCity = :cityId AND m.date >= :since")
    Double averageTemperatureSince(@Param("cityId") Long cityId, @Param("since") LocalDateTime since);

    @Query("SELECT AVG(m.humidity) FROM Measurement m WHERE m.city.idCity = :cityId AND m.date >= :since")
    Double averageHumiditySince(@Param("cityId") Long cityId, @Param("since") LocalDateTime since);

    @Query("SELECT AVG(m.rain) FROM Measurement m WHERE m.city.idCity = :cityId AND m.date >= :since")
    Double averageRainSince(@Param("cityId") Long cityId, @Param("since") LocalDateTime since);

    @Query("SELECT AVG(m.pressure) FROM Measurement m WHERE m.city.idCity = :cityId AND m.date >= :since")
    Double averagePressureSince(@Param("cityId") Long cityId, @Param("since") LocalDateTime since);

    @Query("SELECT AVG(m.wind) FROM Measurement m WHERE m.city.idCity = :cityId AND m.date >= :since")
    Double averageWindSpeedSince(@Param("cityId") Long cityId, @Param("since") LocalDateTime since);

}