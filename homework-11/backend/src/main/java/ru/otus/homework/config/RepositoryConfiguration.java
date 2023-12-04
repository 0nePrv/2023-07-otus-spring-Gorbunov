package ru.otus.homework.config;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.EnableMongock;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableMongock
@EnableReactiveMongoRepositories("ru.otus.homework.repository")
public class RepositoryConfiguration {

  @Bean
  MongoReactiveDriver mongoReactiveDriver(MongoClient mongoClient,
      @Value("${spring.data.mongodb.database}") String databaseName) {
    MongoReactiveDriver driver = MongoReactiveDriver.withDefaultLock(mongoClient, databaseName);
    driver.setWriteConcern(WriteConcern.MAJORITY.withJournal(false)
        .withWTimeout(1000, TimeUnit.MILLISECONDS));
    driver.setReadConcern(ReadConcern.MAJORITY);
    driver.disableTransaction();
    driver.setReadPreference(ReadPreference.primary());
    return driver;
  }
}
