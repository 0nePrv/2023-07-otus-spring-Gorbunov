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
import org.springframework.util.LinkedMultiValueMap;
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.dto.BookDto;
import ru.otus.homework.dto.BookWithGenreAndAuthorNamesDto;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.GenreService;

@DisplayName("Book controller")
@WebMvcTest(BookController.class)
class BookControllerTest {

  private static final AuthorDto EXISTING_AUTHOR = new AuthorDto(1L, "Alexandr Pushkin");

  private static final GenreDto EXISTING_GENRE = new GenreDto(1L, "Novel");

  private static final BookDto EXISTING_BOOK = new BookDto(1L, "Evgeniy Onegin", 1L, 1L);

  private static final BookWithGenreAndAuthorNamesDto EXISTING_BOOK_WITH_NAMES = new BookWithGenreAndAuthorNamesDto(
      1L, "Evgeniy Onegin", 1L, "Evgeniy Onegin", 1L, "Novel");

  private static final String NEW_BOOK_NAME = "A hero of our time";

  private static final long NEW_AUTHOR_ID = 2L;

  private static final long NEW_GENRE_ID = 2L;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BookService bookService;

  @MockBean
  private AuthorService authorService;

  @MockBean
  private GenreService genreService;

  @Test
  @DisplayName("should correctly process GET-request for book creation")
  void shouldCorrectlyCreateNewBook() throws Exception {
    List<AuthorDto> authors = Collections.singletonList(EXISTING_AUTHOR);
    List<GenreDto> genres = Collections.singletonList(EXISTING_GENRE);
    when(authorService.getAll()).thenReturn(authors);
    when(genreService.getAll()).thenReturn(genres);

    mockMvc.perform(get("/book/new"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetBook", new BookDto()))
        .andExpect(model().attribute("authors", authors))
        .andExpect(model().attribute("genres", genres))
        .andExpect(view().name("book/book-add"));

    verify(authorService, times(1)).getAll();
    verify(genreService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process GET-request for book creation and redirect")
  void shouldCorrectlySaveNewBook() throws Exception {
    mockMvc.perform(post("/book/new")
            .params(new LinkedMultiValueMap<>() {{
              add("name", EXISTING_BOOK.getName());
              add("authorId", String.valueOf(EXISTING_BOOK.getAuthorId()));
              add("genreId", String.valueOf(EXISTING_BOOK.getGenreId()));
            }})
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/book"));

    verify(bookService, times(1))
        .add(EXISTING_BOOK.getName(), EXISTING_BOOK.getAuthorId(), EXISTING_BOOK.getGenreId());
  }

  @Test
  @DisplayName("should correctly process GET-request for providing all books")
  void shouldCorrectlyProvideAllBooks() throws Exception {
    List<BookWithGenreAndAuthorNamesDto> books = Collections.singletonList(
        EXISTING_BOOK_WITH_NAMES);
    when(bookService.getAllWithGenreAndAuthorNames()).thenReturn(books);

    mockMvc.perform(get("/book"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("books", books))
        .andExpect(view().name("book/book-list"));

    verify(bookService, times(1)).getAllWithGenreAndAuthorNames();
  }

  @Test
  @DisplayName("should correctly process GET-request for editing book by id")
  void shouldCorrectlyProvideEditingBookById() throws Exception {
    List<AuthorDto> authors = Collections.singletonList(EXISTING_AUTHOR);
    List<GenreDto> genres = Collections.singletonList(EXISTING_GENRE);
    when(bookService.get(EXISTING_BOOK.getId())).thenReturn(EXISTING_BOOK);
    when(authorService.getAll()).thenReturn(authors);
    when(genreService.getAll()).thenReturn(genres);

    mockMvc.perform(get("/book/update")
            .param("id", String.valueOf(EXISTING_AUTHOR.getId())))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetBook", EXISTING_BOOK))
        .andExpect(model().attribute("authors", authors))
        .andExpect(model().attribute("genres", genres))
        .andExpect(view().name("book/book-edit"));

    verify(bookService, times(1)).get(EXISTING_BOOK.getId());
    verify(authorService, times(1)).getAll();
    verify(genreService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process POST-request for updating book and redirect")
  void shouldCorrectlyProvideUpdatingBook() throws Exception {
    mockMvc.perform(post("/book/update")
            .params(new LinkedMultiValueMap<>() {{
              add("id", String.valueOf(EXISTING_BOOK.getId()));
              add("name", NEW_BOOK_NAME);
              add("authorId", String.valueOf(NEW_AUTHOR_ID));
              add("genreId", String.valueOf(NEW_GENRE_ID));
            }}))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/book"));

    verify(bookService, times(1))
        .update(EXISTING_BOOK.getId(), NEW_BOOK_NAME, NEW_AUTHOR_ID, NEW_GENRE_ID);
  }

  @Test
  @DisplayName("should correctly process POST-request for deleting book and redirect")
  void shouldCorrectlyProvideDeletingBook() throws Exception {
    mockMvc.perform(get("/book/delete")
            .param("id", String.valueOf(EXISTING_BOOK.getId())))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/book"));

    verify(bookService, times(1)).remove(EXISTING_BOOK.getId());
  }
}