package ru.otus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.integration.config.EnableIntegrationManagement;
import ru.otus.homework.config.properties.ApplicationProperties;

@SpringBootApplication
@EnableIntegrationManagement
@EnableConfigurationProperties(ApplicationProperties.class)
public class WeatherApplication {

  public static void main(String[] args) {
    SpringApplication.run(WeatherApplication.class, args);
  }
}