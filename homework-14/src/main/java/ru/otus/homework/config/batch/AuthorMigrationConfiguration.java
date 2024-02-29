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
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.processor.AuthorProcessor;
import ru.otus.homework.config.properties.ChunkSizePropertyProvider;

@Configuration
public class AuthorMigrationConfiguration {

  private final ChunkSizePropertyProvider chunkSizePropertyProvider;

  private final JobRepository jobRepository;

  private final PlatformTransactionManager platformTransactionManager;

  public AuthorMigrationConfiguration(ChunkSizePropertyProvider chunkSizePropertyProvider,
      JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    this.chunkSizePropertyProvider = chunkSizePropertyProvider;
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
  }

  @Bean
  public ItemReader<EAuthor> authorItemReader(EntityManagerFactory factory) {
    return new JpaPagingItemReaderBuilder<EAuthor>()
        .name("authorItemReader")
        .entityManagerFactory(factory)
        .queryString("select a from EAuthor a")
        .transacted(true)
        .build();
  }

  @Bean
  public ItemProcessor<EAuthor, DAuthor> authorItemProcessor(AuthorProcessor authorProcessor) {
    return authorProcessor::process;
  }

  @Bean
  public ItemWriter<DAuthor> authorItemWriter(MongoOperations template) {
    return new MongoItemWriterBuilder<DAuthor>()
        .template(template)
        .mode(Mode.INSERT)
        .collection(template.getCollectionName(DAuthor.class))
        .build();
  }

  @Bean
  public Step authorMigrationStep(ItemReader<EAuthor> authorItemReader,
      ItemProcessor<EAuthor, DAuthor> authorItemProcessor,
      ItemWriter<DAuthor> authorItemWriter) {
    return new StepBuilder("authorMigrationStep", jobRepository)
        .<EAuthor, DAuthor>chunk(chunkSizePropertyProvider.getChunkSize(), platformTransactionManager)
        .reader(authorItemReader)
        .processor(authorItemProcessor)
        .writer(authorItemWriter)
        .build();
  }

  @Bean
  public Flow authorMigrationFlow(Step authorMigrationStep) {
    return new FlowBuilder<SimpleFlow>("authorMigrationFlow")
        .start(authorMigrationStep)
        .end();
  }
}
