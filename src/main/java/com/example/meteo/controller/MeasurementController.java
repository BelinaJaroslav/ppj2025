package com.example.meteo.controller;

import com.example.meteo.entity.Measurement;
import com.example.meteo.service.AggregationService;
import com.example.meteo.service.MeasurementService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final AggregationService aggregationService;

    @Autowired
    public MeasurementController(MeasurementService measurementService, AggregationService aggregationService) {
        this.measurementService = measurementService;
        this.aggregationService = aggregationService;
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


    @GetMapping("/{cityId}/aggregates")
    public ResponseEntity<Map<String, Object>> getCityAggregates(@PathVariable Long cityId) {
        Map<String, Object> aggregates = aggregationService.getCityAggregates(cityId);
        return ResponseEntity.ok(aggregates);
    }
}
