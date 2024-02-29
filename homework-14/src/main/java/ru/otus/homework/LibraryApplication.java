package ru.otus.homework;

import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.otus.homework.config.properties.ApplicationProperties;


@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class LibraryApplication {

  public static void main(String... args) {
    SpringApplication.run(LibraryApplication.class, args);
  }

  @ConditionalOnProperty(prefix = "app", name = "h2-server-enabled", havingValue = "true")
  @Bean(initMethod = "start", destroyMethod = "stop")
  public Server h2Server() throws SQLException {
    return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
  }
}
