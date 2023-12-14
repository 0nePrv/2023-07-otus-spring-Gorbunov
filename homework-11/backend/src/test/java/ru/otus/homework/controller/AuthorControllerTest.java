package ru.otus.homework.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import reactor.test.StepVerifier;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.service.AuthorService;

@DisplayName("Author rest controller")
@WebFluxTest(AuthorController.class)
class AuthorControllerTest {

  private static final String BASE_URI_TEMPLATE = "/api/author";

  private static final AuthorDto EXISTING_AUTHOR = new AuthorDto(new ObjectId().toString(),
      "Pushkin");

  private static final String NEW_AUTHOR_NAME = "Lermontov";
  private static final String NEW_AUTHOR_ID = new ObjectId().toString();

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private AuthorService authorService;

  @Test
  @DisplayName("should correctly process POST-request for author creation")
  void shouldCorrectlyCreateNewAuthor() {
    AuthorDto authorDto = new AuthorDto().setName(NEW_AUTHOR_NAME);
    AuthorDto createdAuthorDto = new AuthorDto(NEW_AUTHOR_ID, NEW_AUTHOR_NAME);

    when(authorService.add(any())).thenReturn(Mono.just(createdAuthorDto));

    Flux<AuthorDto> responseBody = webClient.post()
        .uri(BASE_URI_TEMPLATE)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(authorDto)
        .exchange()
        .expectStatus().isCreated()
        .returnResult(AuthorDto.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectNext(createdAuthorDto)
        .verifyComplete();
  }



  @Test
  @DisplayName("should correctly process GET-request for providing all authors")
  void shouldCorrectlyReturnAllAuthors() {
    Flux<AuthorDto> authors = Flux.just(EXISTING_AUTHOR);
    when(authorService.getAll()).thenReturn(authors);

    Flux<AuthorDto> responseBody = webClient.get().uri(BASE_URI_TEMPLATE)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(AuthorDto.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectNext(EXISTING_AUTHOR)
        .verifyComplete();

    verify(authorService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process GET-request for providing author by id")
  void shouldCorrectlyReturnAuthorById() {
    when(authorService.get(EXISTING_AUTHOR.getId())).thenReturn(Mono.just(EXISTING_AUTHOR));

    webClient.get().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_AUTHOR.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthorDto.class)
        .value(authorDto -> assertEquals(EXISTING_AUTHOR, authorDto));

    verify(authorService, times(1)).get(EXISTING_AUTHOR.getId());
  }

  @Test
  @DisplayName("should correctly process PUT-request for updating author")
  void shouldCorrectlyProvideUpdatingAuthor() {
    AuthorDto authorDto = new AuthorDto().setName(NEW_AUTHOR_NAME);
    AuthorDto updatedAuthorDto = new AuthorDto(EXISTING_AUTHOR.getId(), NEW_AUTHOR_NAME);

    when(authorService.update(eq(EXISTING_AUTHOR.getId()), any()))
        .thenReturn(Mono.just(updatedAuthorDto));

    webClient.put().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_AUTHOR.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(authorDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthorDto.class)
        .value(a -> assertThat(a).usingRecursiveComparison().isEqualTo(updatedAuthorDto));
  }

  @Test
  @DisplayName("should correctly process DELETE-request for removing author")
  void shouldCorrectlyProvideDeletingAuthor() {
    when(authorService.remove(EXISTING_AUTHOR.getId())).thenReturn(Mono.empty());

    webClient.delete().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_AUTHOR.getId())
        .exchange()
        .expectStatus().isNoContent();

    verify(authorService, times(1)).remove(EXISTING_AUTHOR.getId());
  }
}
