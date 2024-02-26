package ru.otus.homework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
public class ApplicationProperties {

  private String jobName = "migrationJob";

  private int chunkSize = 10;

  private int cacheSizeLimit = 100;
}
