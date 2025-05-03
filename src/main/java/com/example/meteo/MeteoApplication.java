package com.example.meteo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableJpaRepositories("com.example.meteo.repository")
@EnableWebMvc
@OpenAPIDefinition(
        info = @Info(title = "Meteo API", version = "1.0", description = "Weather API for cities")
)
@SpringBootApplication
@EnableScheduling
public class MeteoApplication {
    private static final Logger log = LoggerFactory.getLogger(MeteoApplication.class);

    public static void main(String[] args) {
        try {
            log.info("Starting MeteoApplication");
            SpringApplication.run(MeteoApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}