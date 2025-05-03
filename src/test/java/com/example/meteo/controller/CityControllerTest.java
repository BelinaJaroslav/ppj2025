package com.example.meteo.controller;

import com.example.meteo.entity.City;
import com.example.meteo.entity.Country;
import com.example.meteo.service.CityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private City testCity;
    private Country testCountry;

    @BeforeEach
    void setUp() {
        testCountry = new Country();
        testCountry.setIdCountry(1);
        testCountry.setName("Test Country");

        testCity = new City("Test City", testCountry, 12.34, 56.78);
        testCity.setIdCity(1);
    }

    @Test
    void getAllCities_ShouldReturnAllCities() {
        // Given
        List<City> expectedCities = Arrays.asList(testCity);
        when(cityService.getAllCities()).thenReturn(expectedCities);

        // When
        List<City> result = cityController.getAllCities();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCity, result.get(0));
        verify(cityService, times(1)).getAllCities();
    }

    @Test
    void getCityById_WhenCityExists_ShouldReturnCity() {
        // Given
        when(cityService.getCityById(1L)).thenReturn(Optional.of(testCity));

        // When
        City result = cityController.getCityById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testCity, result);
        verify(cityService, times(1)).getCityById(1L);
    }

    @Test
    void getCityById_WhenCityNotExists_ShouldThrowException() {
        // Given
        when(cityService.getCityById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> cityController.getCityById(99L));
        verify(cityService, times(1)).getCityById(99L);
    }

    @Test
    void createCity_ShouldReturnCreatedCity() {
        // Given
        when(cityService.createCity(any(City.class))).thenReturn(testCity);

        // When
        City result = cityController.createCity(testCity);

        // Then
        assertNotNull(result);
        assertEquals(testCity, result);
        verify(cityService, times(1)).createCity(testCity);
    }

    @Test
    void updateCity_WhenCityExists_ShouldReturnUpdatedCity() {
        // Given
        when(cityService.updateCity(eq(1L), any(City.class))).thenReturn(testCity);

        // When
        City result = cityController.updateCity(1L, testCity);

        // Then
        assertNotNull(result);
        assertEquals(testCity, result);
        verify(cityService, times(1)).updateCity(1L, testCity);
    }

    @Test
    void deleteCity_ShouldCallService() {
        // Given
        doNothing().when(cityService).deleteCity(1L);

        // When
        cityController.deleteCity(1L);

        // Then
        verify(cityService, times(1)).deleteCity(1L);
    }
}