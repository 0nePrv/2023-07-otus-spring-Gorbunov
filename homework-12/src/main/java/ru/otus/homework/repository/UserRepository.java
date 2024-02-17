package ru.otus.homework.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}