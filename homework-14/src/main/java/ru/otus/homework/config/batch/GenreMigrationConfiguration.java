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
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.service.processor.GenreProcessor;
import ru.otus.homework.config.properties.ChunkSizePropertyProvider;

@Configuration
public class GenreMigrationConfiguration {

  private final ChunkSizePropertyProvider chunkSizePropertyProvider;

  private final JobRepository jobRepository;

  private final PlatformTransactionManager platformTransactionManager;

  public GenreMigrationConfiguration(ChunkSizePropertyProvider chunkSizePropertyProvider,
      JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    this.chunkSizePropertyProvider = chunkSizePropertyProvider;
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
  }

  @Bean
  public ItemReader<EGenre> genreItemReader(EntityManagerFactory factory) {
    return new JpaPagingItemReaderBuilder<EGenre>()
        .name("genreItemReader")
        .entityManagerFactory(factory)
        .queryString("select g from EGenre g")
        .transacted(true)
        .build();
  }

  @Bean
  public ItemProcessor<EGenre, DGenre> genreItemProcessor(GenreProcessor genreProcessor) {
    return genreProcessor::process;
  }


  @Bean
  public ItemWriter<DGenre> genreItemWriter(MongoOperations template) {
    return new MongoItemWriterBuilder<DGenre>()
        .template(template)
        .mode(Mode.INSERT)
        .collection(template.getCollectionName(DGenre.class))
        .build();
  }


  @Bean
  public Step genreMigrationStep(ItemReader<EGenre> genreItemReader,
      ItemProcessor<EGenre, DGenre> genreItemProcessor,
      ItemWriter<DGenre> genreItemWriter) {
    return new StepBuilder("importGenresStep", jobRepository)
        .<EGenre, DGenre>chunk(chunkSizePropertyProvider.getChunkSize(), platformTransactionManager)
        .reader(genreItemReader)
        .processor(genreItemProcessor)
        .writer(genreItemWriter)
        .build();
  }

  @Bean
  public Flow genreMigrationFlow(Step genreMigrationStep) {
    return new FlowBuilder<SimpleFlow>("genreMigrationFlow")
        .start(genreMigrationStep)
        .end();
  }
}
