package com.example.meteo.service;

import com.example.meteo.entity.City;
import com.example.meteo.entity.Country;
import com.example.meteo.entity.Measurement;
import com.example.meteo.repository.CityRepository;
import com.example.meteo.repository.CountryRepository;
import com.example.meteo.repository.MeasurementRepository;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.json.JSONArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;



@Service
public class ScheduledTasks {

    @Autowired
    private MeasurementRepository mr;
    @Autowired
    private CityRepository cr;
    @Autowired
    private CountryRepository sr;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Value("${openweathermap.api.key}")
    private String apiKey;
    @Value("${openweathermap.api.url}")
    private String apiUrl;
    @Value("${weather.api.cities}")
    private List<String> cityList;

    private boolean init = false;

    private final CityService cityService;
    private final MeasurementService measurementService;
    private final RestTemplate restTemplate;

    HttpClient client = HttpClient.newHttpClient();

    @Autowired
    public ScheduledTasks(CityService cityService,
                          MeasurementService measurementService) {
        log.info("DownloadService constructor");
        this.cityService = cityService;
        this.measurementService = measurementService;
        this.restTemplate = new RestTemplate();
    }


    @Scheduled(fixedRateString = "${weather.update.interval}")
    private void downloadMeasurements() {
        if(!init)
            fetchCities();
        log.info("downloadMeasurements");
        List<City> cities = cityService.getAllCities();

        for (City city : cities) {
            try {
                String url = "%s/data/2.5/weather?lat=%f&lon=%f&appid=%s".formatted(
                        apiUrl,
                        city.getLatitude(),
                        city.getLongitude(),
                        apiKey
                );
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
        int dt = JsonPath.read(response, "$.dt");

        Measurement measurement = new Measurement();
        measurement.setCity(city);
        measurement.setTemperature(temperature);
        measurement.setHumidity(humidity);
        measurement.setDate(new Date(dt));

        return measurement;
    }

    public void fetchCities() {
        log.info("fetchCities");
        for (String row : cityList) {
            log.info(row);
            var pieces = row.split("@");
            fetchCity(pieces[0], pieces[1]);
        }
        init=true;
    }
    private void fetchCity(String name, String countryCode)
    {
        var url = "%s/geo/1.0/direct?q=%s,%s&limit=1&appid=%s".formatted(
                apiUrl,
                name,
                countryCode,
                apiKey
        );
        log.trace(url);
        var request = HttpRequest.newBuilder(URI.create(url))
                .header("accept", "application/json")
                .build();
        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(JSONArray::new)
                .join();

        var maybeCountry = sr.findByCode(countryCode);
        var maybeCity = cr.findByName(name);

        if(maybeCountry.isEmpty())
            log.info("Registering new country from code, please edit name: " + countryCode);
        Country country = maybeCountry.orElseGet(() -> sr.save(new Country(countryCode, countryCode)));

        if(maybeCity.isEmpty())
            log.info("Registering new city from code, please edit name: " + name);
        maybeCity.orElseGet(() -> cr.save(new City(
                name,
                country,
                response.getJSONObject(0).getDouble("lat"),
                response.getJSONObject(0).getDouble("lon")
        )));
    }


        private double toDouble(Object input) {
        if (input instanceof Double) {
            return (Double) input;
        } else {
            return ((Integer) input).doubleValue();
        }
    }
}
