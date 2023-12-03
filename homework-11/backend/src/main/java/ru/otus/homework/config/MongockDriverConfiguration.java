package ru.otus.homework.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongockDriverConfiguration {
  @Bean
  MongoReactiveDriver mongoReactiveDriver(MongoClient mongoClient) {
    MongoReactiveDriver driver = MongoReactiveDriver.withDefaultLock(mongoClient, "library");
    driver.setWriteConcern(
        WriteConcern.MAJORITY.withJournal(true).withWTimeout(1000, TimeUnit.MILLISECONDS));
    driver.setReadConcern(ReadConcern.MAJORITY);
    driver.disableTransaction();
    driver.setReadPreference(ReadPreference.primary());
    return driver;
  }
}
