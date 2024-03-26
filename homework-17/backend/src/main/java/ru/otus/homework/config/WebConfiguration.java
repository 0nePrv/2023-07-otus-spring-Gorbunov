package ru.otus.homework.config;

import static org.springframework.http.MediaType.ALL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_XML;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  @Override
  public void configureMessageConverters(@NonNull List<HttpMessageConverter<?>> converters) {
    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
        .indentOutput(true)
        .createXmlMapper(true)
        .dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
        .modulesToInstall(new ParameterNamesModule());
    converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    converters.add(
        new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(APPLICATION_JSON, APPLICATION_XML, ALL);
  }
}
