package ru.otus.homework.service.processor;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.dto.Forecast;
import ru.otus.homework.dto.Weather;
import ru.otus.homework.service.processor.solver.WeatherSolver;

@Service
public class WeatherProcessorImpl implements WeatherProcessor {

  private final List<WeatherSolver> solvers;

  @Autowired
  public WeatherProcessorImpl(List<WeatherSolver> solvers) {
    this.solvers = solvers;
  }

  @Override
  public Forecast process(Weather weather) {
    var forecast = new Forecast(weather.getCity());
    for (var solver : solvers) {
      var decision = solver.solve(weather);
      forecast.addDecision(decision);
    }
    return forecast;
  }
}