package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.User;
import ru.otus.homework.dto.UserDto;

@Component
public class UserToDtoConverter implements Converter<User, UserDto> {

  @Nullable
  @Override
  public UserDto convert(@NonNull User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getPassword(),
        user.getRoles().stream().map(role -> (GrantedAuthority) role::getName).toList());
  }
}
