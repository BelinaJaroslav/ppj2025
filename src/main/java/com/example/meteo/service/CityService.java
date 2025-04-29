package com.example.meteo.service;

import com.example.meteo.controller.CityController;
import com.example.meteo.entity.City;
import com.example.meteo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CityService {

    Logger log = LoggerFactory.getLogger(CityController.class);

    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        log.info("CityService");
        this.cityRepository = cityRepository;
    }

    public List<City> getAllCities() {
        log.info("getAllCities");
        return cityRepository.findAll();
    }

    public Optional<City> getCityById(Long id) {
        log.info("getCityById");
        return cityRepository.findById(id);
    }

    public City createCity(City city) {
        log.info("createCity");
        return cityRepository.save(city);
    }

    public City updateCity(Long id, City cityDetails) {
        log.info("updateCity");
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id " + id));
        city.setName(cityDetails.getName());
        city.setCountry(cityDetails.getCountry());
        return cityRepository.save(city);
    }

    public void deleteCity(Long id) {
        log.info("deleteCity");
        cityRepository.deleteById(id);
    }
}
