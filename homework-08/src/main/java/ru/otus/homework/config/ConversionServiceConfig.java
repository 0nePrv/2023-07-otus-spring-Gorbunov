package ru.otus.homework.config;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ConversionServiceConfig {

  @Bean
  public ConversionServiceFactoryBean conversionService(Set<Converter<?, ?>> converters) {
    ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
    conversionService.setConverters(converters);
    return conversionService;
  }
}
