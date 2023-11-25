package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.homework.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByBookId(long bookId);
}
