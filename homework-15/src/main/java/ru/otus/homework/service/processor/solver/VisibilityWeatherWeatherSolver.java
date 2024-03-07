package ru.otus.homework.service.processor.solver;

import org.springframework.stereotype.Component;
import ru.otus.homework.dto.Weather;
import ru.otus.homework.dto.Decision;
import ru.otus.homework.dto.Metric;

@Component
public class VisibilityWeatherWeatherSolver implements WeatherSolver {

  @Override
  public Decision solve(Weather weather) {
    String visibilityMiles = process(weather.getVisibilityMiles());
    return new Decision(Metric.VISIBILITY, visibilityMiles);
  }

  private String process(double visibilityMiles) {
    if (visibilityMiles < 5) {
      return "Low";
    } else if (visibilityMiles < 10) {
      return "Middle";
    } else {
      return "High";
    }
  }
}
