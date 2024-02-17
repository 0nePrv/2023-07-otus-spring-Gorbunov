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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.dto.GenreDto;
import ru.otus.homework.service.GenreService;

@DisplayName("Genre controller")
@WebMvcTest(GenreController.class)
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private static final GenreDto EXISTING_GENRE = new GenreDto(1L, "Poem");

  private static final String NEW_AUTHOR_NAME = "Lermontov";

  @MockBean
  private GenreService genreService;

  @Test
  @DisplayName("should correctly process GET-request for genre creation")
  void shouldCorrectlyCreateNewGenre() throws Exception {
    mockMvc.perform(get("/genre/new"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("targetGenre", new GenreDto()))
        .andExpect(view().name("genre/genre-add"));
  }

  @Test
  @DisplayName("should correctly process POST-request for genre creation and redirect")
  void shouldCorrectlySaveNewGenre() throws Exception {
    mockMvc.perform(post("/genre/new")
            .param("name", EXISTING_GENRE.getName()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/genre"));

    verify(genreService, times(1)).add(EXISTING_GENRE.getName());
  }

  @Test
  @DisplayName("should correctly process GET-request for providing all genres")
  void shouldCorrectlyProvideAllGenres() throws Exception {
    List<GenreDto> genres = Collections.singletonList(EXISTING_GENRE);
    when(genreService.getAll()).thenReturn(genres);

    mockMvc.perform(get("/genre"))
        .andExpect(status().isOk())
        .andExpect(view().name("genre/genre-list"))
        .andExpect(model().attribute("genres", genres));

    verify(genreService, times(1)).getAll();
  }

  @Test
  @DisplayName("should correctly process GET-request for editing genre by id")
  void shouldCorrectlyProvideEditingGenreById() throws Exception {
    when(genreService.get(EXISTING_GENRE.getId())).thenReturn(EXISTING_GENRE);

    mockMvc.perform(get("/genre/update/" + EXISTING_GENRE.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("genre/genre-edit"))
        .andExpect(model().attribute("targetGenre", EXISTING_GENRE));

    verify(genreService, times(1)).get(EXISTING_GENRE.getId());
  }

  @Test
  @DisplayName("should correctly process POST-request for updating genre and redirect")
  void shouldCorrectlyProvideUpdatingGenre() throws Exception {
    mockMvc.perform(post("/genre/update/" + EXISTING_GENRE.getId())
            .param("name", NEW_AUTHOR_NAME))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/genre"));

    verify(genreService, times(1)).update(EXISTING_GENRE.getId(), NEW_AUTHOR_NAME);
  }

  @Test
  @DisplayName("should correctly process POST-request for deleting genre and redirect")
  void shouldCorrectlyProvideDeletingGenre() throws Exception {
    mockMvc.perform(post("/genre/delete/" + EXISTING_GENRE.getId()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/genre"));

    verify(genreService, times(1)).remove(EXISTING_GENRE.getId());
  }
}