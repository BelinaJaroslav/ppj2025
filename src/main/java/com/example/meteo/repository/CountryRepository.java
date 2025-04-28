package com.example.meteo.repository;

import java.util.Optional;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.meteo.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

@RepositoryRestResource(collectionResourceRel="Country", path="country")
public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
}