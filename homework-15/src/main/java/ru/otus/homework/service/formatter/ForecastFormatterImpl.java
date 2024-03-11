package ru.otus.homework.service.formatter;

import org.springframework.stereotype.Service;
import ru.otus.homework.dto.Forecast;

@Service
public class ForecastFormatterImpl implements ForecastFormatter {

  @Override
  public String format(Forecast forecast) {
    var builder = new StringBuilder();
    builder.append("Forecast for ").append(forecast.getCity()).append("\n");
    forecast.getDecisions().forEach(decision ->
        builder.append("\t").append(decision.toString()).append("\n")
    );
    return builder.toString();
  }
}
