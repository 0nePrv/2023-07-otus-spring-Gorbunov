package ru.otus.homework.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.homework.config.ApplicationProperties;

@Component
public class ChunkSizePropertyProviderImpl implements ChunkSizePropertyProvider {

  private final ApplicationProperties properties;

  @Autowired
  public ChunkSizePropertyProviderImpl(ApplicationProperties properties) {
    this.properties = properties;
  }

  @Override
  public int getChunkSize() {
    return properties.getChunkSize();
  }
}
