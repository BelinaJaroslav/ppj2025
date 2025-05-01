package com.example.meteo.repository;

import com.example.meteo.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(collectionResourceRel="City", path="city")
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByCountry_IdCountry(int countryId);
    Optional<City> findByName(String name);
}