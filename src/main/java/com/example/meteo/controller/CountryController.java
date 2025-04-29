package com.example.meteo.controller;

import com.example.meteo.entity.Country;
import com.example.meteo.service.CountryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    private static final Logger log = LoggerFactory.getLogger(CityController.class);
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        log.info("creating CountryController");
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        log.info("getAllCountries");
        return countryService.getAllCountries();
    }

    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        log.info("getCountryById");
        return countryService.getCountryById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id " + id));
    }

    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        log.info("createCountry");
        return countryService.createCountry(country);
    }

    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable Long id, @RequestBody Country country) {
        log.info("updateCountry");
        return countryService.updateCountry(id, country);
    }

    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Long id) {
        log.info("deleteCountry");
        countryService.deleteCountry(id);
    }
}
