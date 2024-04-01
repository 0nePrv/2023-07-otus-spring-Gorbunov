package ru.otus.homework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@EnableCaching
public class LibraryApplication {

  public static void main(String... args) {
    SpringApplication.run(LibraryApplication.class, args);
    log.info("URL: http://localhost:8080/login");
    log.info("Login: guest\tPassword: guest");
    log.info("Login: user\tPassword: user");
    log.info("Login: admin\tPassword: admin");
  }
}
