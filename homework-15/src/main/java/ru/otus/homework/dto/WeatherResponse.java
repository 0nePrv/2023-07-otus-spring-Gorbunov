package ru.otus.homework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherResponse(@JsonProperty("current") Weather weather,
                              @JsonProperty("location") Location location) {

  public static Weather toWeather(WeatherResponse response) {
    Weather weather = response.weather;
    weather.setCity(response.location.name);
    return weather;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record Location(@JsonProperty("name") String name) {

  }
}