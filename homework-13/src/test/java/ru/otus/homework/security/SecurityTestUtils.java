package ru.otus.homework.security;

import static org.springframework.data.util.Pair.of;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.otus.homework.dto.UserDto;

public class SecurityTestUtils {

  private static final GrantedAuthority GUEST_AUTHORITY = () -> "ROLE_GUEST";

  private static final GrantedAuthority USER_AUTHORITY = () -> "ROLE_USER";

  private static final GrantedAuthority ADMIN_AUTHORITY = () -> "ROLE_ADMIN";

  private static final List<UserDetails> USERS = List.of(
      new UserDto(1L, "any", "any", Collections.emptyList()),
      new UserDto(2L, "guest", "guest", List.of(GUEST_AUTHORITY)),
      new UserDto(3L, "user", "user", List.of(USER_AUTHORITY)),
      new UserDto(4L, "admin", "admin", List.of(ADMIN_AUTHORITY))
  );

  private static final List<String> RESOURCES = List.of(
      "author", "genre", "book", "book/1/comment");

  private static final List<Pair<String, HttpMethod>> ACTIONS = List.of(
      of("", GET),
      of("/new", GET),
      of("/new", POST),
      of("/update/1", GET),
      of("/update/1", POST),
      of("/delete/1", POST));

  private static final Verifier VERIFIER_1 = new Verifier(
      entry -> entry.getUri().equals("/") || entry.getUri().equals("/book"),
      entity -> !entity.getUser().getAuthorities().isEmpty());

  private static final Verifier VERIFIER_2 = new Verifier(
      entry -> entry.getUri().startsWith("/book"),
      entity -> entity.getUser().getAuthorities().contains(USER_AUTHORITY) ||
          entity.getUser().getAuthorities().contains(ADMIN_AUTHORITY));

  private static final Verifier VERIFIER_3 = new Verifier(
      entity -> entity.getUri().startsWith("/author") || entity.getUri().startsWith("/genre"),
      entity -> entity.getUser().getAuthorities().contains(ADMIN_AUTHORITY));


  static Stream<TestEntity> generateTestEntities() {
    return RESOURCES.stream().flatMap(resource ->
        ACTIONS.stream().flatMap(action ->
            USERS.stream().map(user -> createTestEntity(resource, action, user)
            )
        )
    );
  }

  static Stream<Pair<String, HttpMethod>> generateResourceActionPairs() {
    return RESOURCES.stream().flatMap(resource ->
        ACTIONS.stream()
            .map(action -> Pair.of("/" + resource + action.getFirst(), action.getSecond()))
    );
  }

  static List<Verifier> getVerifiers() {
    return List.of(VERIFIER_1, VERIFIER_2, VERIFIER_3);
  }

  private static TestEntity createTestEntity(String resource, Pair<String, HttpMethod> action,
      UserDetails user) {
    return new TestEntity("/" + resource + action.getFirst(), action.getSecond(), user, false);
  }

  @Getter
  @AllArgsConstructor
  static class TestEntity {

    private final String uri;

    private final HttpMethod method;

    private final UserDetails user;

    @Setter
    private boolean result;

    @Override
    public String toString() {
      return "{" +
          "uri='" + uri + '\'' +
          ", method=" + method +
          ", user=" + user.getUsername() +
          ", result=" + result +
          '}';
    }
  }

  @RequiredArgsConstructor
  static class Verifier {

    private final Predicate<TestEntity> matcher;

    private final Predicate<TestEntity> solver;

    public boolean matches(TestEntity entity) {
      return matcher.test(entity);
    }

    public boolean solve(TestEntity entity) {
      return solver.test(entity);
    }
  }
}
