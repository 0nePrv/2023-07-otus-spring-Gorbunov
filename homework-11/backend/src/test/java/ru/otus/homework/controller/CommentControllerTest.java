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
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.service.CommentService;

@DisplayName("Comment controller")
@WebFluxTest(CommentController.class)
class CommentControllerTest {

  private static final String BASE_URI_TEMPLATE = "/api/comment";

  private static final String EXISTING_BOOK_ID = new ObjectId().toString();

  private static final String NEW_COMMENT_BOOK_ID = new ObjectId().toString();

  private static final String NEW_COMMENT_ID = new ObjectId().toString();

  private static final String EXISTING_COMMENT_TEXT = "Some text";

  private static final String NEW_COMMENT_TEXT = "New comment text";

  private static final CommentDto EXISTING_COMMENT = new CommentDto(new ObjectId().toString(),
      "Some text", EXISTING_BOOK_ID);

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private CommentService commentService;

  @Test
  @DisplayName("should correctly process POST-request for comment creation")
  void shouldCorrectlyCreateNewComment() {
    CommentDto commentDto = new CommentDto().setBookId(EXISTING_BOOK_ID)
        .setText(EXISTING_COMMENT_TEXT);
    CommentDto createdCommentDto = new CommentDto(NEW_COMMENT_ID,
        EXISTING_BOOK_ID, EXISTING_COMMENT_TEXT);

    when(commentService.add(any())).thenReturn(Mono.just(createdCommentDto));

    webTestClient.post()
        .uri(BASE_URI_TEMPLATE)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(commentDto)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(CommentDto.class)
        .value(
            comment -> assertThat(comment).usingRecursiveComparison().isEqualTo(createdCommentDto));
  }

  @Test
  @DisplayName("should correctly process GET-request for providing all comments by book id")
  void shouldCorrectlyReturnAllCommentsByBookId() {
    Flux<CommentDto> comments = Flux.fromIterable(Collections.singletonList(EXISTING_COMMENT));

    when(commentService.getByBookId(EXISTING_BOOK_ID)).thenReturn(comments);

    webTestClient.get().uri(BASE_URI_TEMPLATE + "?bookId=" + EXISTING_BOOK_ID)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(CommentDto.class)
        .value(
            commentList -> assertThat(commentList).usingRecursiveComparison()
                .isEqualTo(comments.collectList().block()));

    verify(commentService, times(1)).getByBookId(EXISTING_BOOK_ID);
  }

  @Test
  @DisplayName("should correctly process GET-request for providing comment by id")
  void shouldCorrectlyReturnCommentById() {
    when(commentService.get(EXISTING_COMMENT.getId())).thenReturn(Mono.just(EXISTING_COMMENT));

    webTestClient.get().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_COMMENT.getId())
        .exchange()
        .expectStatus().isOk()
        .expectBody(CommentDto.class)
        .value(
            comment -> assertThat(comment).usingRecursiveComparison().isEqualTo(EXISTING_COMMENT));
  }

  @Test
  @DisplayName("should correctly process PUT-request for updating comment")
  void shouldCorrectlyProvideUpdatingComment() {
    CommentDto commentDto = new CommentDto().setBookId(NEW_COMMENT_BOOK_ID)
        .setText(NEW_COMMENT_TEXT);

    CommentDto updatedCommentDto = new CommentDto(EXISTING_COMMENT.getBookId(),
        NEW_COMMENT_BOOK_ID, NEW_COMMENT_TEXT);

    when(commentService.update(eq(EXISTING_COMMENT.getId()), any()))
        .thenReturn(Mono.just(updatedCommentDto));

    webTestClient.put().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_COMMENT.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(commentDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody(CommentDto.class)
        .value(
            comment -> assertThat(comment).usingRecursiveComparison().isEqualTo(updatedCommentDto));
  }

  @Test
  @DisplayName("should correctly process DELETE-request for removing comment")
  void shouldCorrectlyProvideDeletingComment() {
    webTestClient.delete().uri(BASE_URI_TEMPLATE + "/{id}", EXISTING_COMMENT.getId())
        .exchange()
        .expectStatus().isNoContent();

    verify(commentService, times(1)).remove(EXISTING_COMMENT.getId());
  }
}

