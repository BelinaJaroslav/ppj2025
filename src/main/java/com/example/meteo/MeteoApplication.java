package com.example.meteo;

import com.example.meteo.controller.CityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MeteoApplication {
    private static final Logger log = LoggerFactory.getLogger(CityController.class);

    public static void main(String[] args) {
        try {
            log.info("Starting MeteoApplication");
            SpringApplication.run(MeteoApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}