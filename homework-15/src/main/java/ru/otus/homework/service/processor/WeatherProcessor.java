package ru.otus.homework.service.processor;

import ru.otus.homework.dto.Forecast;
import ru.otus.homework.dto.Weather;

public interface WeatherProcessor {

  @SuppressWarnings("unused")
  Forecast process(Weather weather);
}