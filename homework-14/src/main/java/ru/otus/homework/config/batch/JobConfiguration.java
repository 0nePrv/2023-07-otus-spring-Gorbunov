package ru.otus.homework.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.homework.service.caching.CachingService;
import ru.otus.homework.config.properties.JobNamePropertyProvider;

@Configuration
public class JobConfiguration {

  private final JobNamePropertyProvider jobNamePropertyProvider;

  private final JobRepository jobRepository;

  private final PlatformTransactionManager transactionManager;

  @Autowired
  public JobConfiguration(JobNamePropertyProvider jobNamePropertyProvider,
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    this.jobNamePropertyProvider = jobNamePropertyProvider;
    this.jobRepository = jobRepository;
    this.transactionManager = transactionManager;
  }

  @Bean
  public Tasklet cleanUpTasklet(CachingService cachingService) {
    MethodInvokingTaskletAdapter tasklet = new MethodInvokingTaskletAdapter();
    tasklet.setTargetObject(cachingService);
    tasklet.setTargetMethod("clear");
    return tasklet;
  }

  @Bean
  public Step cleanUpStep(Tasklet cleanUpTasklet) {
    return new StepBuilder("cleanUpStep", jobRepository)
        .tasklet(cleanUpTasklet, transactionManager)
        .build();
  }

  @Bean
  public Flow authorAndGenreAsyncMigrationFlow(Flow authorMigrationFlow, Flow genreMigrationFlow) {
    return new FlowBuilder<SimpleFlow>("authorAndGenreMigrationFlow")
        .split(new SimpleAsyncTaskExecutor("migration"))
        .add(authorMigrationFlow, genreMigrationFlow)
        .build();
  }

  @Bean
  public Job migrationJob(Flow authorAndGenreAsyncMigrationFlow, Flow bookMigrationFlow,
      Flow commentMigrationFlow, Step cleanUpStep) {
    return new JobBuilder(jobNamePropertyProvider.getJobName(), jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(authorAndGenreAsyncMigrationFlow)
        .next(bookMigrationFlow)
        .next(commentMigrationFlow)
        .next(cleanUpStep)
        .end()
        .build();
  }
}
