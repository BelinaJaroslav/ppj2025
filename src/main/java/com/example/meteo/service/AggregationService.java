package com.example.meteo.service;

import com.example.meteo.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AggregationService {

    @Autowired
    private MeasurementRepository measurementRepository;

    public Map<String, Object> getCityAggregates(Long cityId) {
        LocalDateTime now = LocalDateTime.now();

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("avgTemperature", Map.of(
                "1d", measurementRepository.averageTemperatureSince(cityId, now.minusDays(1)),
                "7d", measurementRepository.averageTemperatureSince(cityId, now.minusDays(7)),
                "14d", measurementRepository.averageTemperatureSince(cityId, now.minusDays(14))
        ));

        result.put("avgHumidity", Map.of(
                "1d", measurementRepository.averageHumiditySince(cityId, now.minusDays(1)),
                "7d", measurementRepository.averageHumiditySince(cityId, now.minusDays(7)),
                "14d", measurementRepository.averageHumiditySince(cityId, now.minusDays(14))
        ));

        result.put("avgRain", Map.of(
                "1d", measurementRepository.averageRainSince(cityId, now.minusDays(1)),
                "7d", measurementRepository.averageRainSince(cityId, now.minusDays(7)),
                "14d", measurementRepository.averageRainSince(cityId, now.minusDays(14))
        ));

        result.put("avgPressure", Map.of(
                "1d", measurementRepository.averagePressureSince(cityId, now.minusDays(1)),
                "7d", measurementRepository.averagePressureSince(cityId, now.minusDays(7)),
                "14d", measurementRepository.averagePressureSince(cityId, now.minusDays(14))
        ));

        result.put("avgWind", Map.of(
                "1d", measurementRepository.averageWindSpeedSince(cityId, now.minusDays(1)),
                "7d", measurementRepository.averageWindSpeedSince(cityId, now.minusDays(7)),
                "14d", measurementRepository.averageWindSpeedSince(cityId, now.minusDays(14))
        ));

        return result;
    }
}

