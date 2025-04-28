package com.example.meteo.service;

import com.example.meteo.entity.City;
import com.example.meteo.entity.Measurement;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class DownloadService {

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
        this.cityService = cityService;
        this.measurementService = measurementService;
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.restTemplate = new RestTemplate();

        startUpdatingData();
    }

    private void startUpdatingData() {
        threadPoolTaskScheduler.scheduleAtFixedRate(this::downloadMeasurements, UPDATE_INTERVAL);
    }

    private void downloadMeasurements() {
        List<City> cities = cityService.getAllCities();

        for (City city : cities) {
            try {
                String url = apiUrl + "?id=" + city.getIdCity() + "&units=metric&appid=" + apiKey;
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
}
