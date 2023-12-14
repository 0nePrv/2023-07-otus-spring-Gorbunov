package ru.otus.homework.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.service.GenreService;

@DisplayName("Genre rest controller")
@WebFluxTest(GenreController.class)
class GenreControllerTest {

  private static final String BASE_URI_TEMPLATE = "/api/genre";

  private static final GenreDto EXISTING_GENRE = new GenreDto(new Object().toString(), "Drama");

  private static final String NEW_GENRE_NAME = "Novel";

  private static final String NEW_GENRE_ID = new Object().toString();

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private GenreService genreService;

  @Test
  @DisplayName("should correctly process POST-request for genre creation")
  void shouldCorrectlyCreateNewGenre() {
    GenreDto genreDto = new GenreDto().setName(NEW_GENRE_NAME);
    GenreDto createdGenreDto = new GenreDto(NEW_GENRE_ID, NEW_GENRE_NAME);

    when(genreService.add(any())).thenReturn(Mono.just(createdGenreDto));

    Flux<GenreDto> responseBody = webClient.post()
        .uri(BASE_URI_TEMPLATE)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(genreDto)
        .exchange()
        .expectStatus().isCreated()
        .returnResult(GenreDto.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectNext(createdGenreDto)
        .verifyComplete();
  }


  @Test
  @DisplayName("should correctly process GET-request for providing all genres")
  void shouldCorrectlyReturnAllGenres() {
    Flux<GenreDto> genres = Flux.just(EXISTING_GENRE);
    when(genreService.getAll()).thenReturn(genres);

    Flux<GenreDto> responseBody = webClient.get().uri(BASE_URI_TEMPLATE)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(GenreDto.class)
        .getResponseBody();

    StepVerifier.create(responseBody)
        .expectNext(EXISTING_GENRE)
        .verifyComplete();

    verify(genreService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process GET-request for providing genre by id")
  void shouldCorrectlyReturnGenreById() {
    when(genreService.get(EXISTING_GENRE.getId())).thenReturn(Mono.just(EXISTING_GENRE));

    webClient.get().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_GENRE.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(GenreDto.class)
        .value(genreDto -> assertEquals(EXISTING_GENRE, genreDto));

    verify(genreService, times(1)).get(EXISTING_GENRE.getId());
  }

  @Test
  @DisplayName("should correctly process PUT-request for updating genre")
  void shouldCorrectlyProvideUpdatingGenre() {
    GenreDto genreDto = new GenreDto().setName(NEW_GENRE_NAME);
    GenreDto updatedGenreDto = new GenreDto(EXISTING_GENRE.getId(), NEW_GENRE_NAME);

    when(genreService.update(eq(EXISTING_GENRE.getId()), any()))
        .thenReturn(Mono.just(updatedGenreDto));

    webClient.put().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_GENRE.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(genreDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(GenreDto.class)
        .value(a -> assertThat(a).usingRecursiveComparison().isEqualTo(updatedGenreDto));
  }

  @Test
  @DisplayName("should correctly process DELETE-request for removing genre")
  void shouldCorrectlyProvideDeletingGenre() {
    when(genreService.remove(EXISTING_GENRE.getId())).thenReturn(Mono.empty());

    webClient.delete().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_GENRE.getId())
        .exchange()
        .expectStatus().isNoContent();

    verify(genreService, times(1)).remove(EXISTING_GENRE.getId());
  }
}

