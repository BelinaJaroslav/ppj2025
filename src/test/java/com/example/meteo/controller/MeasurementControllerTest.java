package com.example.meteo.controller;

import com.example.meteo.entity.City;
import com.example.meteo.entity.Country;
import com.example.meteo.entity.Measurement;
import com.example.meteo.service.AggregationService;
import com.example.meteo.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeasurementControllerTest {

    @Mock
    private MeasurementService measurementService;

    @Mock
    private AggregationService aggregationService;

    @InjectMocks
    private MeasurementController measurementController;

    private Measurement testMeasurement;
    private City testCity;

    @BeforeEach
    void setUp() {
        // Initialize the controller manually to ensure proper injection
        measurementController = new MeasurementController(measurementService, aggregationService); // Add setter in controller

        testCity = new City("Test City", new Country(), 12.34, 56.78);
        testCity.setIdCity(1);

        Date testDate = new Date();
        testMeasurement = new Measurement(
                testCity,
                testDate,
                25.5,  // temperature
                1013.2, // pressure
                65.0,   // humidity
                10.2,   // wind
                0.0     // rain
        );
        testMeasurement.setIdMeasurement(1);
        testMeasurement.setTimestamp(Instant.now());
    }

    @Test
    void getAllMeasurements_ShouldReturnAllMeasurements() {
        // Given
        List<Measurement> expectedMeasurements = Arrays.asList(testMeasurement);
        when(measurementService.getAllMeasurements()).thenReturn(expectedMeasurements);

        // When
        List<Measurement> result = measurementController.getAllMeasurements();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMeasurement, result.get(0));
        verify(measurementService, times(1)).getAllMeasurements();
    }

    @Test
    void getMeasurementById_WhenMeasurementExists_ShouldReturnMeasurement() {
        // Given
        Long measurementId = 1L;
        when(measurementService.getMeasurementById(measurementId)).thenReturn(Optional.of(testMeasurement));

        // When
        Measurement result = measurementController.getMeasurementById(measurementId);

        // Then
        assertNotNull(result);
        assertEquals(testMeasurement, result);
        verify(measurementService, times(1)).getMeasurementById(measurementId);
    }

    @Test
    void getMeasurementById_WhenMeasurementNotExists_ShouldThrowException() {
        // Given
        Long measurementId = 99L;
        when(measurementService.getMeasurementById(measurementId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> measurementController.getMeasurementById(measurementId));
        verify(measurementService, times(1)).getMeasurementById(measurementId);
    }

    @Test
    void createMeasurement_ShouldReturnCreatedMeasurement() {
        // Given
        when(measurementService.createMeasurement(any(Measurement.class))).thenReturn(testMeasurement);

        // When
        Measurement result = measurementController.createMeasurement(testMeasurement);

        // Then
        assertNotNull(result);
        assertEquals(testMeasurement, result);
        verify(measurementService, times(1)).createMeasurement(testMeasurement);
    }

    @Test
    void updateMeasurement_WhenMeasurementExists_ShouldReturnUpdatedMeasurement() {
        // Given
        Long measurementId = 1L;
        when(measurementService.updateMeasurement(eq(measurementId), any(Measurement.class))).thenReturn(testMeasurement);

        // When
        Measurement result = measurementController.updateMeasurement(measurementId, testMeasurement);

        // Then
        assertNotNull(result);
        assertEquals(testMeasurement, result);
        verify(measurementService, times(1)).updateMeasurement(measurementId, testMeasurement);
    }

    @Test
    void deleteMeasurement_ShouldCallService() {
        // Given
        Long measurementId = 1L;
        doNothing().when(measurementService).deleteMeasurement(measurementId);

        // When
        measurementController.deleteMeasurement(measurementId);

        // Then
        verify(measurementService, times(1)).deleteMeasurement(measurementId);
    }

    @Test
    void getCityAggregates_ShouldReturnAggregatedData() {
        // Given
        Long cityId = 1L;
        Map<String, Object> mockAggregates = new HashMap<>();
        mockAggregates.put("avgTemperature", 22.5);
        mockAggregates.put("maxPressure", 1020.0);

        when(aggregationService.getCityAggregates(cityId)).thenReturn(mockAggregates);

        // When
        ResponseEntity<Map<String, Object>> response = measurementController.getCityAggregates(cityId);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockAggregates, response.getBody());
        verify(aggregationService, times(1)).getCityAggregates(cityId);
    }
}