package ru.otus.homework.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.http.dsl.HttpMessageHandlerSpec;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.client.RestTemplate;
import ru.otus.homework.config.properties.ApiKeyProvider;
import ru.otus.homework.config.properties.RequestDelayProvider;
import ru.otus.homework.dto.Weather;
import ru.otus.homework.dto.WeatherResponse;
import ru.otus.homework.service.city.CityService;
import ru.otus.homework.service.processor.WeatherProcessor;

@Configuration
public class IntegrationConfiguration {

  @Bean
  public SubscribableChannel inputChannel() {
    return new PublishSubscribeChannel();
  }

  @Bean
  public PollableChannel outputChannel() {
    return new QueueChannel(10);
  }

  @Bean
  public AsyncTaskExecutor exec() {
    return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(2));
  }

  @Bean
  public PollerSpec inputChannelPoller(RequestDelayProvider requestDelayProvider) {
    return Pollers.fixedDelay(requestDelayProvider.getRequestDelay())
        .taskExecutor(exec())
        .maxMessagesPerPoll(1)
        .receiveTimeout(10_000L);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplateBuilder()
        .rootUri("http://api.weatherapi.com/v1")
        .setConnectTimeout(Duration.ofMillis(5_000L))
        .additionalMessageConverters(new MappingJackson2HttpMessageConverter())
        .build();
  }

  @Bean
  public HttpMessageHandlerSpec httpMessageHandler(ApiKeyProvider provider, CityService city) {
    return Http.outboundGateway("/current.json?key={apiKey}&q={city}", restTemplate())
        .httpMethod(HttpMethod.GET)
        .uriVariable("apiKey", message -> provider.getApiKey())
        .uriVariable("city", message -> city.getRandom().getName())
        .expectedResponseType(WeatherResponse.class);
  }

  @Bean
  public IntegrationFlow fetchingSubFlow(HttpMessageHandlerSpec httpMessageHandler,
      PollerMetadata inputChannelPoller) {
    return IntegrationFlow.from(() -> new GenericMessage<>(""),
            pollerSpec -> pollerSpec.poller(inputChannelPoller))
        .handle(httpMessageHandler).log(Level.DEBUG)
        .transform(WeatherResponse::toWeather)
        .channel(inputChannel())
        .get();
  }

  @Bean
  public IntegrationFlow flow(WeatherProcessor weatherProcessor) {
    return IntegrationFlow.from(inputChannel())
        .<Weather, Weather>transform(
            weather -> weather.setTemperature(weather.getTemperature() * 9 / 5 + 32))
        .<Weather>filter(weather ->
            weather.getLastUpdated().isAfter(LocalDateTime.now().minusDays(1)))
        .<Weather>log(Level.INFO, weatherMessage ->
            "Weather to process:\n%s".formatted(weatherMessage.getPayload()))
        .handle(weatherProcessor, "process")
        .channel(outputChannel())
        .get();
  }
}