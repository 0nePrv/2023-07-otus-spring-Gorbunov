package ru.otus.homework.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.homework.config.properties.RequestDelayProvider;
import ru.otus.homework.dto.City;
import ru.otus.homework.dto.Forecast;
import ru.otus.homework.dto.Weather;
import ru.otus.homework.service.ForecastGateway;
import ru.otus.homework.service.city.CityService;
import ru.otus.homework.service.processor.WeatherProcessor;

@SpringBootTest
@ContextConfiguration(classes = IntegrationFlowTestConfiguration.class)
@DisplayName("Integration flow test")
class IntegrationFlowTest {

  @Autowired
  private IntegrationFlow flow;

  @Autowired
  private ForecastGateway forecastGateway;

  @Autowired
  private RequestDelayProvider requestDelayProvider;

  @Autowired
  private CityService cityService;

  @MockBean
  private WeatherProcessor processor;

  @Test
  @DirtiesContext
  @DisplayName("should correctly process weather data and return valid forecast payload")
  void shouldCorrectlyProcessWeatherDataAndReturnValidForecastPayload() {
    String cityName = cityService.getRandom().getName();
    Weather weather = new Weather(cityName, LocalDateTime.now(),
        10.6, 11.9, 1019.0, 4.0);
    Forecast expectedForecast = new Forecast(cityName);
    Message<Weather> message = new GenericMessage<>(weather);

    when(processor.process(any(Weather.class))).thenReturn(expectedForecast);

    MessageChannel inputChannel = flow.getInputChannel();
    assertThat(inputChannel).isNotNull();
    inputChannel.send(message);

    Forecast forecast = assertDoesNotThrow(() -> forecastGateway.predictAsync()
        .get(requestDelayProvider.getRequestDelay(), TimeUnit.MILLISECONDS));
    assertThat(forecast).isNotNull().isEqualTo(expectedForecast);
  }

  @Test
  @DisplayName("should filter outdated weather data and throw timeout exception")
  void shouldFilterOutdatedWeatherDataAndReturnNull() {
    Weather weather = new Weather(City.WASHINGTON.getName(), LocalDateTime.now().minusMonths(1),
        10.6, 11.9, 1019.0, 4.0);
    Message<Weather> message = new GenericMessage<>(weather);

    MessageChannel inputChannel = flow.getInputChannel();
    assertThat(inputChannel).isNotNull();
    inputChannel.send(message);

    assertThrowsExactly(TimeoutException.class, () -> forecastGateway.predictAsync()
        .get(requestDelayProvider.getRequestDelay(), TimeUnit.MILLISECONDS));
  }
}