package com.example.meteo.controller;

import com.example.meteo.entity.City;
import com.example.meteo.service.CityService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private static final Logger log = LoggerFactory.getLogger(CityController.class);

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        log.info("Creating City Controller");
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> getAllCities() {
        log.info("Getting all City List");
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public City getCityById(@PathVariable Long id) {
        log.info("Getting City ById");
        return cityService.getCityById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id " + id));
    }

    @PostMapping
    public City createCity(@RequestBody City city) {
        log.info("Creating City");
        return cityService.createCity(city);
    }

    @PutMapping("/{id}")
    public City updateCity(@PathVariable Long id, @RequestBody City city) {
        log.info("Updating City");
        return cityService.updateCity(id, city);
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable Long id) {
        log.info("Deleting City");
        cityService.deleteCity(id);
    }


}
