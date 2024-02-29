package ru.otus.homework.config.batch;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter.Mode;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.homework.config.properties.ChunkSizePropertyProvider;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.relational.EBook;
import ru.otus.homework.processor.BookProcessor;

@Configuration
public class BookMigrationConfiguration {

  private final ChunkSizePropertyProvider chunkSizePropertyProvider;

  private final JobRepository jobRepository;

  private final PlatformTransactionManager platformTransactionManager;

  public BookMigrationConfiguration(ChunkSizePropertyProvider chunkSizePropertyProvider,
      JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    this.chunkSizePropertyProvider = chunkSizePropertyProvider;
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
  }

  @Bean
  public ItemReader<EBook> bookItemReader(EntityManagerFactory factory) {
    return new JpaPagingItemReaderBuilder<EBook>()
        .name("bookItemReader")
        .entityManagerFactory(factory)
        .queryString("select b from EBook b join fetch b.author join fetch b.genre")
        .transacted(true)
        .build();
  }

  @Bean
  public ItemProcessor<EBook, DBook> bookItemProcessor(BookProcessor bookService) {
    return bookService::process;
  }

  @Bean
  public ItemWriter<DBook> bookItemWriter(MongoOperations template) {
    return new MongoItemWriterBuilder<DBook>()
        .template(template)
        .mode(Mode.INSERT)
        .collection(template.getCollectionName(DBook.class))
        .build();
  }

  @Bean
  public Step bookMigrationStep(ItemReader<EBook> bookItemReader,
      ItemProcessor<EBook, DBook> bookItemProcessor,
      ItemWriter<DBook> bookItemWriter) {
    return new StepBuilder("bookMigrationStep", jobRepository)
        .<EBook, DBook>chunk(chunkSizePropertyProvider.getChunkSize(), platformTransactionManager)
        .reader(bookItemReader)
        .processor(bookItemProcessor)
        .writer(bookItemWriter)
        .build();
  }

  @Bean
  public Flow bookMigrationFlow(Step bookMigrationStep) {
    return new FlowBuilder<SimpleFlow>("bookMigrationFlow")
        .start(bookMigrationStep)
        .end();
  }
}
