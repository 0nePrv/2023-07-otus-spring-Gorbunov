package ru.otus.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import ru.otus.homework.repository.projection.BookProjection;
import ru.otus.homework.repository.projection.CommentProjection;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

  @Override
  public void configureRepositoryRestConfiguration(
      RepositoryRestConfiguration repositoryRestConfiguration, CorsRegistry cors) {
    repositoryRestConfiguration.getProjectionConfiguration()
        .addProjection(CommentProjection.class)
        .addProjection(BookProjection.class);
  }
}
