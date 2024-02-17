package ru.otus.homework.security;

import static org.junit.jupiter.params.provider.Arguments.of;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.homework.config.SecurityConfig;
import ru.otus.homework.domain.User;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.CommentService;
import ru.otus.homework.service.GenreService;

@WebMvcTest
@DisplayName("Authentication test")
@Import({SecurityConfig.class})
@MockBean(classes = {AuthorService.class, BookService.class, CommentService.class,
    GenreService.class})
public class AuthenticationTest {

  private static final UserDetails TEST_USER = new User("user", "password");

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("should provide endpoint access to authenticated user")
  @ParameterizedTest
  @MethodSource("getMethodAndURI")
  public void shouldProvideEndpointAccessToAuthenticatedUser(URI uri, HttpMethod method)
      throws Exception {
    mockMvc.perform(request(method, uri).with(user(TEST_USER)))
        .andExpect(authenticated());
  }

  @DisplayName("should not provide endpoint access to not authenticated user")
  @ParameterizedTest
  @MethodSource("getMethodAndURI")
  public void shouldNotProvideEndpointAccessToNotAuthenticatedUser(URI uri, HttpMethod method)
      throws Exception {
    mockMvc.perform(request(method, uri)).andExpect(unauthenticated());
  }

  public static Stream<Arguments> getMethodAndURI() {
    var resources = List.of("author", "genre", "book", "book/1/comment");
    var actions = List.of(
        of("", GET),
        of("/new", GET),
        of("/new", POST),
        of("/update/1", GET),
        of("/update/1", POST),
        of("/delete/1", POST)
    );
    return resources.stream().flatMap(mapArgs(actions));
  }

  private static Function<String, Stream<Arguments>> mapArgs(List<Arguments> actions) {
    return resource -> actions.stream()
        .map(action -> of(
            URI.create("/" + resource + action.get()[0]), action.get()[1]));
  }
}