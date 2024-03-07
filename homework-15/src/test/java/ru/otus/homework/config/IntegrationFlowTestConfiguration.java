package ru.otus.homework.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import ru.otus.homework.config.properties.ApplicationProperties;
import ru.otus.homework.service.ForecastGateway;

@SpringBootConfiguration
@EnableIntegration
@EnableConfigurationProperties(ApplicationProperties.class)
@IntegrationComponentScan(basePackageClasses = ForecastGateway.class)
@ComponentScan(basePackages = {"ru.otus.homework.config", "ru.otus.homework.service.city"})
public class IntegrationFlowTestConfiguration {

}