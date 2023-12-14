package ru.otus.homework.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.service.BookService;

@DisplayName("Book rest controller")
@WebFluxTest(BookController.class)
class BookControllerTest {

  private static final String BASE_URI_TEMPLATE = "/api/book";

  private static final BookDto EXISTING_BOOK = new BookDto(new ObjectId().toString(),
      "Evgeniy Onegin", new ObjectId().toString(), "Alexandr Pushkin",
      new ObjectId().toString(), "Novel");

  private static final String NEW_BOOK_NAME = "A hero of our time";

  private static final String NEW_AUTHOR_ID = new ObjectId().toString();

  private static final String NEW_BOOK_ID = new ObjectId().toString();

  private static final String NEW_GENRE_ID = new ObjectId().toString();

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private BookService bookService;


  @Test
  @DisplayName("should correctly process POST-request for book creation")
  void shouldCorrectlyCreateNewBook() {
    BookDto bookDto = new BookDto();
    bookDto.setName(NEW_BOOK_NAME);
    bookDto.setAuthorId(NEW_AUTHOR_ID);
    bookDto.setGenreId(NEW_GENRE_ID);

    BookDto createdBookDto = new BookDto(NEW_BOOK_ID, NEW_BOOK_NAME, NEW_AUTHOR_ID,
        "AuthorName", NEW_GENRE_ID, "GenreName");

    when(bookService.add(any())).thenReturn(Mono.just(createdBookDto));

    webTestClient.post()
        .uri(BASE_URI_TEMPLATE)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(bookDto)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(BookDto.class)
        .value(book -> assertThat(book).usingRecursiveComparison().isEqualTo(createdBookDto));
  }

  @Test
  @DisplayName("should correctly process GET-request for providing all books with author and genre names")
  void shouldCorrectlyProvideAllBooksWithNames() {
    when(bookService.getAll()).thenReturn(Flux.just(EXISTING_BOOK));

    webTestClient.get().uri(BASE_URI_TEMPLATE)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(BookDto.class)
        .value(bookList -> assertThat(bookList).usingRecursiveComparison()
            .isEqualTo(Collections.singletonList(EXISTING_BOOK)));

    verify(bookService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process GET-request for providing book by id")
  void shouldCorrectlyReturnBookById() {

    when(bookService.get(EXISTING_BOOK.getId())).thenReturn(Mono.just(EXISTING_BOOK));

    webTestClient.get().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_BOOK.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(BookDto.class)
        .value(book -> assertThat(book).usingRecursiveComparison().isEqualTo(EXISTING_BOOK));

    verify(bookService, times(1)).get(EXISTING_BOOK.getId());
  }

  @Test
  @DisplayName("should correctly process PUT-request for updating book")
  void shouldCorrectlyProvideUpdatingBook() {
    BookDto bookDto = new BookDto();
    bookDto.setName(NEW_BOOK_NAME);
    bookDto.setAuthorId(NEW_AUTHOR_ID);
    bookDto.setGenreId(NEW_GENRE_ID);

    BookDto updatedBookDto = new BookDto(EXISTING_BOOK.getId(), NEW_BOOK_NAME,
        NEW_AUTHOR_ID, "AuthorName", NEW_GENRE_ID, "GenreName");

    when(bookService.update(eq(EXISTING_BOOK.getId()), any()))
        .thenReturn(Mono.just(updatedBookDto));

    webTestClient.put().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_BOOK.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(bookDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(BookDto.class)
        .value(book -> assertThat(book).usingRecursiveComparison().isEqualTo(updatedBookDto));
  }

  @Test
  @DisplayName("should correctly process DELETE-request for removing book")
  void shouldCorrectlyProvideDeletingBook() {
    webTestClient.delete().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_BOOK.getId())
        .exchange()
        .expectStatus().isNoContent();

    verify(bookService, times(1)).remove(EXISTING_BOOK.getId());
  }
}

