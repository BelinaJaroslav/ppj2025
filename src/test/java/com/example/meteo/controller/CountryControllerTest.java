package com.example.meteo.controller;

import com.example.meteo.entity.Country;
import com.example.meteo.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryControllerTest {

    @Mock
    private CountryService countryService;

    @Mock
    private Logger logger;

    @InjectMocks
    private CountryController countryController;

    private Country testCountry;

    @BeforeEach
    void setUp() {
        testCountry = new Country("Test Country", "TC");
        testCountry.setIdCountry(1);
    }

    @Test
    void getAllCountries_ShouldReturnAllCountries() {
        // Given
        List<Country> expectedCountries = Arrays.asList(testCountry);
        when(countryService.getAllCountries()).thenReturn(expectedCountries);

        // When
        List<Country> result = countryController.getAllCountries();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCountry, result.get(0));
        verify(countryService, times(1)).getAllCountries();
    }

    @Test
    void getCountryById_WhenCountryExists_ShouldReturnCountry() {
        // Given
        Long countryId = 1L;
        when(countryService.getCountryById(countryId)).thenReturn(Optional.of(testCountry));

        // When
        Country result = countryController.getCountryById(countryId);

        // Then
        assertNotNull(result);
        assertEquals(testCountry, result);
        verify(countryService, times(1)).getCountryById(countryId);
    }

    @Test
    void getCountryById_WhenCountryNotExists_ShouldThrowException() {
        // Given
        Long countryId = 99L;
        when(countryService.getCountryById(countryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> countryController.getCountryById(countryId));
        verify(countryService, times(1)).getCountryById(countryId);
    }

    @Test
    void createCountry_ShouldReturnCreatedCountry() {
        // Given
        when(countryService.createCountry(any(Country.class))).thenReturn(testCountry);

        // When
        Country result = countryController.createCountry(testCountry);

        // Then
        assertNotNull(result);
        assertEquals(testCountry, result);
        verify(countryService, times(1)).createCountry(testCountry);
    }

    @Test
    void updateCountry_WhenCountryExists_ShouldReturnUpdatedCountry() {
        // Given
        Long countryId = 1L;
        when(countryService.updateCountry(eq(countryId), any(Country.class))).thenReturn(testCountry);

        // When
        Country result = countryController.updateCountry(countryId, testCountry);

        // Then
        assertNotNull(result);
        assertEquals(testCountry, result);
        verify(countryService, times(1)).updateCountry(countryId, testCountry);
    }

    @Test
    void deleteCountry_ShouldCallService() {
        // Given
        Long countryId = 1L;
        doNothing().when(countryService).deleteCountry(countryId);

        // When
        countryController.deleteCountry(countryId);

        // Then
        verify(countryService, times(1)).deleteCountry(countryId);
    }
}