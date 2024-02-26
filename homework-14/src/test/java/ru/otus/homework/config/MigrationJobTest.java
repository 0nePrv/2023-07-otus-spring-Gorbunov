package ru.otus.homework.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.mongo.DComment;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.domain.relational.EBook;
import ru.otus.homework.domain.relational.EComment;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.provider.JobNamePropertyProvider;

@SpringBootTest
@SpringBatchTest
@DisplayName("Migration job test")
class MigrationJobTest {

  public static final String AUTHOR_QUERY = "select a from EAuthor a";
  public static final String GENRE_QUERY = "select g from EGenre g";
  public static final String BOOK_QUERY = "select b from EBook b join fetch b.author join fetch b.genre";
  public static final String COMMENT_QUERY = "select c from EComment c";

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JobRepositoryTestUtils jobRepositoryTestUtils;

  @Autowired
  private JobNamePropertyProvider jobNamePropertyProvider;

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private MongoOperations mongoTemplate;

  private List<EAuthor> existingAuthors;

  private List<EGenre> existingGenres;

  private List<EBook> existingBooks;

  private List<EComment> existingComments;

  @BeforeEach
  @Transactional
  void prepare() {
    jobRepositoryTestUtils.removeJobExecutions();

    existingAuthors = entityManager.createQuery(AUTHOR_QUERY, EAuthor.class).getResultList();
    existingGenres = entityManager.createQuery(GENRE_QUERY, EGenre.class).getResultList();
    existingBooks = entityManager.createQuery(BOOK_QUERY, EBook.class).getResultList();
    existingComments = entityManager.createQuery(COMMENT_QUERY, EComment.class).getResultList();
  }


  @Test
  @DisplayName("should migrate all data correctly")
  void shouldMigrateAllDataCorrectly() throws Exception {
    Job job = jobLauncherTestUtils.getJob();

    assertThat(job).isNotNull()
        .extracting(Job::getName)
        .isEqualTo(jobNamePropertyProvider.getJobName());

    JobExecution jobExecution = jobLauncherTestUtils.launchJob();
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

    checkAuthors();
    checkGenres();
    checkBooks();
    checkComments();
  }

  private void checkAuthors() {
    List<DAuthor> authors = mongoTemplate.findAll(DAuthor.class);
    assertThat(authors).hasSizeGreaterThan(0)
        .extracting(DAuthor::getName)
        .containsAll(existingAuthors.stream()
            .map(EAuthor::getName)
            .collect(Collectors.toList()));
  }

  private void checkGenres() {
    List<DGenre> genres = mongoTemplate.findAll(DGenre.class);
    assertThat(genres).hasSizeGreaterThan(0)
        .extracting(DGenre::getName)
        .containsAll(existingGenres.stream()
            .map(EGenre::getName)
            .collect(Collectors.toList()));
  }

  private void checkBooks() {
    List<DBook> books = mongoTemplate.findAll(DBook.class);
    assertThat(books).hasSizeGreaterThan(0)
        .extracting(DBook::getName, b -> b.getAuthor().getName(), b -> b.getGenre().getName())
        .containsAll(existingBooks.stream()
            .map(b -> tuple(b.getName(), b.getAuthor().getName(), b.getGenre().getName()))
            .collect(Collectors.toList()));
  }

  private void checkComments() {
    List<DComment> comments = mongoTemplate.findAll(DComment.class);
    assertThat(comments).hasSizeGreaterThan(0)
        .extracting(DComment::getText)
        .containsAll(existingComments.stream()
            .map(EComment::getText)
            .collect(Collectors.toList()));
  }
}