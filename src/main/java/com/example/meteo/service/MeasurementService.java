package com.example.meteo.service;

import com.example.meteo.entity.Measurement;
import com.example.meteo.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.lang.System.currentTimeMillis;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public Optional<Measurement> getMeasurementById(Long id) {
        return measurementRepository.findById(id);
    }

    public Measurement createMeasurement(Measurement measurement) {
        measurement.setDate(new Date(currentTimeMillis()));
        return measurementRepository.save(measurement);
    }

    public Measurement updateMeasurement(Long id, Measurement measurementDetails) {
        Measurement measurement = measurementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found with id " + id));
        measurement.setTemperature(measurementDetails.getTemperature());
        measurement.setHumidity(measurementDetails.getHumidity());
        measurement.setCity(measurementDetails.getCity());
        return measurementRepository.save(measurement);
    }

    public void deleteMeasurement(Long id) {
        measurementRepository.deleteById(id);
    }

    public List<Measurement> getMeasurementsForCitySince(Long cityId, Instant since) {
        return measurementRepository.findByCityIdAndTimestampAfter(cityId, since);
    }
}
