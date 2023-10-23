package ru.otus.homework.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
@DisplayName("Book repository")
class BookRepositoryTest {

  private static final int EXPECTED_NUMBER_OF_BOOKS = 9;

  @Autowired
  private BookRepository bookService;

  @Test
  @DisplayName("should return correct books")
  void shouldReturnBooks() {
    assertThat(bookService.findAll()).isNotNull().hasSize(EXPECTED_NUMBER_OF_BOOKS)
        .allMatch(b -> b.getId() != null)
        .allMatch(b -> b.getName() != null && !b.getName().isEmpty())
        .allMatch(b -> b.getAuthor() != null && b.getAuthor().getId() != null)
        .allMatch(b -> b.getGenre() != null && b.getGenre().getId() != null);
  }
}