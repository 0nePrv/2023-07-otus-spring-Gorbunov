package ru.otus.homework.service.processor.solver;

import org.springframework.stereotype.Component;
import ru.otus.homework.dto.Decision;
import ru.otus.homework.dto.Metric;
import ru.otus.homework.dto.Weather;

@Component
public class TemperatureWeatherWeatherSolver implements WeatherSolver {

  @Override
  public Decision solve(Weather weather) {
    String temperatureDecision = process(weather.getTemperature());
    return new Decision(Metric.TEMPERATURE_F, temperatureDecision);
  }

  private String process(double temperature) {
    if (temperature < 32) {
      return "Cold";
    } else if (temperature < 59) {
      return "Cool";
    } else {
      return "Hot";
    }
  }
}
