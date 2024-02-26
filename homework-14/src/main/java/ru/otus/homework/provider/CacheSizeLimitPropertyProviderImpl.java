package ru.otus.homework.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationProperties;

@Component
public class CacheSizeLimitPropertyProviderImpl implements CacheSizeLimitPropertyProvider {

  private final ApplicationProperties properties;

  @Autowired
  public CacheSizeLimitPropertyProviderImpl(ApplicationProperties properties) {
    this.properties = properties;
  }

  @Override
  public int getCacheSizeLimit() {
    return properties.getCacheSizeLimit();
  }
}
