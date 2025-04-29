package com.example.meteo.controller;

import com.example.meteo.entity.Measurement;
import com.example.meteo.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping
    public List<Measurement> getAllMeasurements() {
        return measurementService.getAllMeasurements();
    }

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable Long id) {
        return measurementService.getMeasurementById(id)
                .orElseThrow(() -> new RuntimeException("Measurement not found with id " + id));
    }

    @PostMapping
    public Measurement createMeasurement(@RequestBody Measurement measurement) {
        return measurementService.createMeasurement(measurement);
    }

    @PutMapping("/{id}")
    public Measurement updateMeasurement(@PathVariable Long id, @RequestBody Measurement measurement) {
        return measurementService.updateMeasurement(id, measurement);
    }

    @DeleteMapping("/{id}")
    public void deleteMeasurement(@PathVariable Long id) {
        measurementService.deleteMeasurement(id);
    }

    // Zvláštní endpoint na získání měření pro město za určité období
    @GetMapping("/city/{cityId}/since/{millis}")
    public List<Measurement> getMeasurementsForCitySince(@PathVariable Long cityId, @PathVariable long millis) {
        Instant since = Instant.ofEpochMilli(millis);
        return measurementService.getMeasurementsForCitySince(cityId, since);
    }
}
