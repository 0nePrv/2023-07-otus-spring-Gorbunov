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
import ru.otus.homework.domain.mongo.DComment;
import ru.otus.homework.domain.relational.EComment;
import ru.otus.homework.processor.CommentProcessor;
import ru.otus.homework.config.properties.ChunkSizePropertyProvider;

@Configuration
public class CommentMigrationConfiguration {

  private final ChunkSizePropertyProvider chunkSizePropertyProvider;

  private final JobRepository jobRepository;

  private final PlatformTransactionManager platformTransactionManager;

  public CommentMigrationConfiguration(ChunkSizePropertyProvider chunkSizePropertyProvider,
      JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    this.chunkSizePropertyProvider = chunkSizePropertyProvider;
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
  }

  @Bean
  public ItemReader<EComment> commentItemReader(EntityManagerFactory factory) {
    return new JpaPagingItemReaderBuilder<EComment>()
        .name("commentItemReader")
        .entityManagerFactory(factory)
        .pageSize(10)
        .queryString("select c from EComment c")
        .transacted(true)
        .build();
  }

  @Bean
  public ItemProcessor<EComment, DComment> commentItemProcessor(CommentProcessor commentProcessor) {
    return commentProcessor::process;
  }

  @Bean
  public ItemWriter<DComment> commentItemWriter(MongoOperations template) {
    return new MongoItemWriterBuilder<DComment>()
        .template(template)
        .mode(Mode.INSERT)
        .collection(template.getCollectionName(DComment.class))
        .build();
  }

  @Bean
  public Step commentMigrationStep(ItemReader<EComment> commentItemReader,
      ItemProcessor<EComment, DComment> commentItemProcessor,
      ItemWriter<DComment> commentItemWriter) {
    return new StepBuilder("commentMigrationStep", jobRepository)
        .<EComment, DComment>chunk(chunkSizePropertyProvider.getChunkSize(), platformTransactionManager)
        .reader(commentItemReader)
        .processor(commentItemProcessor)
        .writer(commentItemWriter)
        .build();
  }

  @Bean
  public Flow commentMigrationFlow(Step commentMigrationStep) {
    return new FlowBuilder<SimpleFlow>("commentMigrationFlow")
        .start(commentMigrationStep)
        .end();
  }
}
