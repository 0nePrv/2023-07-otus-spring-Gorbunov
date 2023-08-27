package ru.otus.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

@Configuration
@PropertySource("classpath:app-config.properties")
public class AppConfig {

    @Bean
    public ConversionServiceFactoryBean conversionService(Set<Converter<?, ?>> converters) {
        ConversionServiceFactoryBean conversionServiceFactoryBean = new ConversionServiceFactoryBean();
        conversionServiceFactoryBean.setConverters(converters);
        return conversionServiceFactoryBean;
    }
}
