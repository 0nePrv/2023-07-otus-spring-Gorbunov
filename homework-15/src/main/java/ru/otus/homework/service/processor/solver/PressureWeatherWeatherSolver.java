package ru.otus.homework.service.processor.solver;

import org.springframework.stereotype.Component;
import ru.otus.homework.dto.Weather;
import ru.otus.homework.dto.Decision;
import ru.otus.homework.dto.Metric;

@Component
public class PressureWeatherWeatherSolver implements WeatherSolver {

  @Override
  public Decision solve(Weather weather) {
    String pressureSolved = processTemperature(weather.getPressureMb());
    return new Decision(Metric.PRESSURE, pressureSolved);
  }

  private String processTemperature(double pressure) {
    if (pressure < 1015) {
      return "Low";
    } else if (pressure > 1020) {
      return "High";
    }
    return "Normal";
  }
}
