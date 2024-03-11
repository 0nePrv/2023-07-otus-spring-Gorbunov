package ru.otus.homework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application", ignoreInvalidFields = true)
public class ApplicationProperties implements ApiKeyProvider, RequestDelayProvider {

  private String apiKey;

  private long requestDelay = 5_000L;
}
