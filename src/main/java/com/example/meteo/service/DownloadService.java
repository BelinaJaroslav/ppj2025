package com.example.meteo.service;

import com.example.meteo.entity.City;
import com.example.meteo.entity.Measurement;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class DownloadService {

    private static Logger log = LoggerFactory.getLogger(DownloadService.class);
    private static final long UPDATE_INTERVAL = 1000 * 60 * 10; // 10 minut
    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Value("${openweathermap.api.url}")
    private String apiUrl;

    private final CityService cityService;
    private final MeasurementService measurementService;
    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final RestTemplate restTemplate;

    @Autowired
    public DownloadService(CityService cityService,
                           MeasurementService measurementService,
                           ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        log.info("DownloadService constructor");
        this.cityService = cityService;
        this.measurementService = measurementService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.restTemplate = new RestTemplate();

        startUpdatingData();
    }

    private void startUpdatingData() {
        log.info("startUpdatingData");
        threadPoolTaskScheduler.scheduleAtFixedRate(this::downloadMeasurements, UPDATE_INTERVAL);
    }

    private void downloadMeasurements() {
        log.info("downloadMeasurements");
        List<City> cities = cityService.getAllCities();

        for (City city : cities) {
            try {
                String url = apiUrl + "?id=" + city.getId() + "&units=metric&appid=" + apiKey;
                String response = restTemplate.getForObject(url, String.class);
                Measurement measurement = parseMeasurement(response, city);

                measurementService.createMeasurement(measurement);
            } catch (Exception e) {
                System.err.println("Failed to download data for city: " + city.getName());
                e.printStackTrace();
            }
        }
    }

    private Measurement parseMeasurement(String response, City city) {

        log.info("parseMeasurement");

        double temperature = toDouble(JsonPath.read(response, "$.main.temp"));
        int humidity = JsonPath.read(response, "$.main.humidity");
        long dt = JsonPath.read(response, "$.dt");

        Measurement measurement = new Measurement();
        measurement.setCity(city);
        measurement.setTemperature(temperature);
        measurement.setHumidity(humidity);
        measurement.setDate(new Date(dt));

        return measurement;
    }

    private double toDouble(Object input) {
        if (input instanceof Double) {
            return (Double) input;
        } else {
            return ((Integer) input).doubleValue();
        }
    }

    @Configuration
    public static class TaskSchedulerConfig {

        @Bean
        public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
            log.info("threadPoolTaskScheduler");
            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(5); // velikost thread poolu
            scheduler.setThreadNamePrefix("Scheduler-");
            scheduler.initialize();
            return scheduler;
        }
    }
}
