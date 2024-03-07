package ru.otus.homework;


import static java.util.concurrent.TimeUnit.MILLISECONDS;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.properties.RequestDelayProvider;
import ru.otus.homework.service.ForecastGateway;
import ru.otus.homework.service.formatter.ForecastFormatter;

@Slf4j
@Component
public class WeatherApplicationRunner implements ApplicationRunner {

  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  private final RequestDelayProvider provider;

  private final ForecastGateway gateway;

  private final ForecastFormatter forecastFormatter;

  @Autowired
  public WeatherApplicationRunner(RequestDelayProvider provider,
      ForecastGateway gateway, ForecastFormatter forecastFormatter) {
    this.provider = provider;
    this.gateway = gateway;
    this.forecastFormatter = forecastFormatter;
  }

  @Override
  public void run(ApplicationArguments args) {
    Runnable runnable = () -> gateway.predictAsync()
        .whenComplete((forecast, throwable) -> {
          if (throwable != null) {
            log.error("Error occurred while processing weather data: ", throwable);
          } else if (forecast == null) {
            log.warn("No forecast present");
          } else {
            log.info(forecastFormatter.format(forecast));
          }
        });
    executor.scheduleWithFixedDelay(runnable, 0L, provider.getRequestDelay(), MILLISECONDS);
  }

  @PreDestroy
  private void shutDownExecutor() {
    executor.shutdown();
  }
}