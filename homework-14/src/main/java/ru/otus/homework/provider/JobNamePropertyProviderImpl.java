package ru.otus.homework.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationProperties;

@Component
public class JobNamePropertyProviderImpl implements JobNamePropertyProvider {

  private final ApplicationProperties properties;

  @Autowired
  public JobNamePropertyProviderImpl(ApplicationProperties properties) {
    this.properties = properties;
  }

  @Override
  public String getJobName() {
    return properties.getJobName();
  }
}
