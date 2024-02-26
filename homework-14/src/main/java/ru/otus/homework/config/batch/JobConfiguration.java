package ru.otus.homework.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import ru.otus.homework.provider.JobNamePropertyProvider;

@Configuration
public class JobConfiguration {

  private final JobNamePropertyProvider jobNamePropertyProvider;

  private final JobRepository jobRepository;

  @Autowired
  public JobConfiguration(JobNamePropertyProvider jobNamePropertyProvider,
      JobRepository jobRepository) {
    this.jobNamePropertyProvider = jobNamePropertyProvider;
    this.jobRepository = jobRepository;
  }

  @Bean
  public Flow authorAndGenreAsyncMigrationFlow(Flow authorMigrationFlow, Flow genreMigrationFlow) {
    return new FlowBuilder<SimpleFlow>("authorAndGenreMigrationFlow")
        .split(new SimpleAsyncTaskExecutor("migration"))
        .add(authorMigrationFlow, genreMigrationFlow)
        .build();
  }

  @Bean
  public Flow cleanUpFlow(Step authorCleanUpStep, Step bookCleanUpStep, Step genreCleanUpStep) {
    return new FlowBuilder<SimpleFlow>("cleanUpFlow")
        .start(bookCleanUpStep)
        .next(authorCleanUpStep)
        .next(genreCleanUpStep)
        .build();
  }

  @Bean
  public Job migrationJob(Flow authorAndGenreAsyncMigrationFlow, Flow bookMigrationFlow,
      Flow commentMigrationFlow, Flow cleanUpFlow) {
    return new JobBuilder(jobNamePropertyProvider.getJobName(), jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(authorAndGenreAsyncMigrationFlow)
        .next(bookMigrationFlow)
        .next(commentMigrationFlow)
        .next(cleanUpFlow)
        .end()
        .build();
  }
}
