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
import ru.otus.homework.dto.AuthorDto;
import ru.otus.homework.service.AuthorService;

@DisplayName("Author controller")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

  private static final AuthorDto EXISTING_AUTHOR = new AuthorDto(1L, "Pushkin");

  private static final String NEW_AUTHOR_NAME = "Lermontov";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthorService authorService;

  @Test
  @DisplayName("should correctly process GET-request for author creation")
  void shouldCorrectlyCreateNewAuthor() throws Exception {
    mockMvc.perform(get("/author/new"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetAuthor", new AuthorDto()))
        .andExpect(view().name("author/author-add"));
  }

  @Test
  @DisplayName("should correctly process POST-request for author creation and redirect")
  void shouldCorrectlySaveNewAuthor() throws Exception {
    mockMvc.perform(post("/author/new")
            .param("name", EXISTING_AUTHOR.getName()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/author"));

    verify(authorService, times(1)).add(EXISTING_AUTHOR.getName());
  }

  @Test
  @DisplayName("should correctly process GET-request for providing all authors")
  void shouldCorrectlyProvideAllAuthors() throws Exception {
    List<AuthorDto> authors = Collections.singletonList(EXISTING_AUTHOR);
    when(authorService.getAll()).thenReturn(authors);

    mockMvc.perform(get("/author"))
        .andExpect(status().isOk())
        .andExpect(view().name("author/author-list"))
        .andExpect(model().attribute("authors", authors));

    verify(authorService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process GET-request for editing author by id")
  void shouldCorrectlyProvideEditingAuthorById() throws Exception {
    when(authorService.get(EXISTING_AUTHOR.getId())).thenReturn(EXISTING_AUTHOR);

    mockMvc.perform(get("/author/update/" + EXISTING_AUTHOR.getId()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetAuthor", EXISTING_AUTHOR))
        .andExpect(view().name("author/author-edit"));

    verify(authorService, times(1)).get(EXISTING_AUTHOR.getId());
  }

  @Test
  @DisplayName("should correctly process POST-request for updating author and redirect")
  void shouldCorrectlyProvideUpdatingAuthor() throws Exception {
    mockMvc.perform(post("/author/update/" + EXISTING_AUTHOR.getId())
            .param("name", NEW_AUTHOR_NAME))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/author"));

    verify(authorService, times(1)).update(EXISTING_AUTHOR.getId(), NEW_AUTHOR_NAME);
  }

  @Test
  @DisplayName("should correctly process POST-request for deleting author and redirect")
  void shouldCorrectlyProvideDeletingAuthor() throws Exception {
    mockMvc.perform(post("/author/delete/" + EXISTING_AUTHOR.getId()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/author"));

    verify(authorService, times(1)).remove(EXISTING_AUTHOR.getId());
  }
}