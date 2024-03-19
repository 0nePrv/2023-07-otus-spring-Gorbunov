package ru.otus.homework.monitor;

import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class BookEndpointAvailbilityHealthIndicator implements HealthIndicator {

  private final int port;

  public BookEndpointAvailbilityHealthIndicator(@Value("${server.port}") int port) {
    this.port = port;
  }

  @Override
  public Health health() {
    try {
      HttpResponse<Void> response = performRequest();
      HttpStatusCode statusCode = HttpStatus.valueOf(response.statusCode());
      if (statusCode.isError()) {
        return Health.down().withDetail("Status code", response.statusCode()).build();
      }
      return Health.up().build();
    } catch (Exception exception) {
      return Health.down().withException(exception).build();
    }
  }

  private HttpResponse<Void> performRequest() throws Exception {
    URL url = new URL("http", "localhost", port, "/book");
    HttpRequest httpRequest = HttpRequest.newBuilder().GET().uri(url.toURI()).build();
    return HttpClient.newHttpClient().send(httpRequest, BodyHandlers.discarding());
  }
}
