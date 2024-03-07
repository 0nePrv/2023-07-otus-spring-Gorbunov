package ru.otus.homework.service;

import java.util.concurrent.CompletableFuture;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.homework.dto.Forecast;

@MessagingGateway(asyncExecutor = "exec")
public interface ForecastGateway {

  @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
  CompletableFuture<Forecast> predictAsync();
}