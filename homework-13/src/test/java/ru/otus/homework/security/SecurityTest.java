package ru.otus.homework.security;

import static org.hamcrest.Matchers.oneOf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.homework.security.SecurityTestUtils.generateTestEntities;
import static ru.otus.homework.security.SecurityTestUtils.getVerifiers;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.otus.homework.config.SecurityConfig;
import ru.otus.homework.security.SecurityTestUtils.TestEntity;
import ru.otus.homework.service.AuthorService;
import ru.otus.homework.service.BookService;
import ru.otus.homework.service.CommentService;
import ru.otus.homework.service.GenreService;

@WebMvcTest
@DisplayName("Security test")
@Import({SecurityConfig.class})
@MockBean(classes = {AuthorService.class, BookService.class, CommentService.class,
    GenreService.class})
public class SecurityTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName("should provide access to public resources")
  @ParameterizedTest
  @ValueSource(strings = {"/login", "/css/login-form-style.css"})
  void shouldProvideAccessToPublicResources(String uri) throws Exception {
    mockMvc.perform(get(uri).with(anonymous())).andExpect(status().isOk());
  }

  @DisplayName("should check endpoint access to target user")
  @ParameterizedTest
  @MethodSource("getTestParams")
  void shouldCheckEndpointAccessToTargetUser(TestEntity entity) throws Exception {
    mockMvc.perform(request(entity.getMethod(), entity.getUri()).with(csrf())
            .with(user(entity.getUser()))).andExpect(getExpectedResult(entity));
  }

  private static ResultMatcher getExpectedResult(TestEntity entity) {
    return entity.isResult() ? status().is(oneOf(200, 302)) : status().isForbidden();
  }

  private static Stream<TestEntity> getTestParams() {
    return generateTestEntities().map(entity -> {
      for (var verifier : getVerifiers()) {
        if (verifier.matches(entity)) {
          entity.setResult(verifier.solve(entity));
          return entity;
        }
      }
      return entity;
    });
  }
}