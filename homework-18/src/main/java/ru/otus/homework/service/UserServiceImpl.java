package ru.otus.homework.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.User;
import ru.otus.homework.dto.UserDto;
import ru.otus.homework.repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  private final ConversionService conversionService;

  @Autowired
  public UserServiceImpl(UserRepository userRepository,
      @Qualifier("mvcConversionService") ConversionService conversionService) {
    this.userRepository = userRepository;
    this.conversionService = conversionService;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() ->
            new UsernameNotFoundException("User with name %s not found".formatted(username)));
    return conversionService.convert(user, UserDto.class);
  }
}
