package ru.otus.homework.controller;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.CommentDto;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.CommentService;

@DisplayName("Comment controller")
@WebMvcTest(CommentController.class)
class CommentControllerTest {

  private static final BookDto EXISTING_BOOK = new BookDto(1L, "Evgeniy Onegin", 1L, 1L);

  private static final CommentDto EXISTING_COMMENT = new CommentDto(1L, "Some text",
      EXISTING_BOOK.getId());

  private static final long NEW_COMMENT_BOOK_ID = 2L;

  private static final String NEW_COMMENT_TEXT = "New comment text";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CommentService commentService;

  @MockBean
  private BookService bookService;

  @Test
  @DisplayName("should correctly process GET-request for comment creation")
  void shouldCorrectlyCreateNewComment() throws Exception {
    List<BookDto> books = Collections.singletonList(EXISTING_BOOK);
    when(bookService.getAll()).thenReturn(books);

    mockMvc.perform(get("/book/{bookId}/comment/new", EXISTING_BOOK.getId()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetComment", new CommentDto(EXISTING_BOOK.getId())))
        .andExpect(model().attribute("books", books))
        .andExpect(view().name("comment/comment-add"));

    verify(bookService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process POST-request for comment creation and redirect")
  void shouldCorrectlySaveNewComment() throws Exception {
    mockMvc.perform(post("/book/{bookId}/comment/new", EXISTING_BOOK.getId())
            .param("bookId", String.valueOf(EXISTING_COMMENT.getBookId()))
            .param("text", EXISTING_COMMENT.getText()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(String.format("/book/%d/comment", EXISTING_COMMENT.getBookId())));

    verify(commentService, times(1))
        .add(EXISTING_COMMENT.getBookId(), EXISTING_COMMENT.getText());
  }

  @Test
  @DisplayName("should correctly process GET-request for providing all comments by book id")
  void shouldCorrectlyProvideAllCommentsByBookId() throws Exception {
    List<CommentDto> comments = Collections.singletonList(EXISTING_COMMENT);
    when(bookService.get(EXISTING_BOOK.getId())).thenReturn(EXISTING_BOOK);
    when(commentService.getByBookId(EXISTING_COMMENT.getBookId())).thenReturn(comments);

    mockMvc.perform(get("/book/{bookId}/comment", EXISTING_BOOK.getId()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("comments", comments))
        .andExpect(model().attribute("targetBook", EXISTING_BOOK))
        .andExpect(view().name("comment/comment-list"));

    verify(bookService, times(1)).get(EXISTING_BOOK.getId());
    verify(commentService, times(1)).getByBookId(EXISTING_COMMENT.getBookId());
  }

  @Test
  @DisplayName("should correctly process GET-request for editing comment by id")
  void shouldCorrectlyProvideEditingCommentById() throws Exception {
    List<BookDto> books = Collections.singletonList(EXISTING_BOOK);
    when(commentService.get(EXISTING_COMMENT.getId())).thenReturn(EXISTING_COMMENT);
    when(bookService.getAll()).thenReturn(books);

    mockMvc.perform(get("/book/{bookId}/comment/update/" + EXISTING_COMMENT.getId(),
            EXISTING_BOOK.getId()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetComment", EXISTING_COMMENT))
        .andExpect(model().attribute("books", books))
        .andExpect(view().name("comment/comment-edit"));

    verify(commentService, times(1)).get(EXISTING_COMMENT.getId());
    verify(bookService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process POST-request for updating comment and redirect")
  void shouldCorrectlyProvideUpdatingComment() throws Exception {
    mockMvc.perform(post("/book/{bookId}/comment/update/" + EXISTING_COMMENT.getId(),
            EXISTING_BOOK.getId())
            .param("bookId", String.valueOf(NEW_COMMENT_BOOK_ID))
            .param("text", NEW_COMMENT_TEXT))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(String.format("/book/%d/comment", NEW_COMMENT_BOOK_ID)));

    verify(commentService, times(1))
        .update(EXISTING_COMMENT.getId(), NEW_COMMENT_BOOK_ID, NEW_COMMENT_TEXT);
  }

  @Test
  @DisplayName("should correctly process POST-request for deleting comment and redirect")
  void shouldCorrectlyProvideDeletingComment() throws Exception {
    mockMvc.perform(post("/book/{bookId}/comment/delete/" + EXISTING_COMMENT.getId(),
            EXISTING_BOOK.getId()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(String.format("/book/%d/comment", EXISTING_BOOK.getId())));

    verify(commentService, times(1)).remove(EXISTING_COMMENT.getId());
  }
}