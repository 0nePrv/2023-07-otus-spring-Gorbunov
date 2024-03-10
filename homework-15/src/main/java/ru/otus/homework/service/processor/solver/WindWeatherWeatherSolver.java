package ru.otus.homework.service.processor.solver;

import org.springframework.stereotype.Component;
import ru.otus.homework.dto.Decision;
import ru.otus.homework.dto.Metric;
import ru.otus.homework.dto.Weather;

@Component
public class WindWeatherWeatherSolver implements WeatherSolver {

  @Override
  public Decision solve(Weather weather) {
    String windDecision = decide(weather.getWindMph());
    return new Decision(Metric.WIND, windDecision);
  }

  private String decide(double getWindMph) {
    if (getWindMph < 10) {
      return "Slow";
    } else if (getWindMph < 24) {
      return "Normal";
    } else {
      return "Fast";
    }
  }
}
