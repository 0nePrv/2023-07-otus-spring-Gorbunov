package ru.otus.homework.shell;

import java.time.LocalDateTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellCommands {

  private final Job migrationJob;

  private final JobLauncher launcher;

  @Autowired
  public ShellCommands(Job migrationJob, JobLauncher launcher) {
    this.migrationJob = migrationJob;
    this.launcher = launcher;
  }

  @ShellMethod(value = "startMigration", key = "sm")
  public String startMigration() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder()
        .addLocalDateTime("date-time", LocalDateTime.now())
        .toJobParameters();
    JobExecution jobExecution = launcher.run(migrationJob, jobParameters);
    return jobExecution.toString();
  }
}