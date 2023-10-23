package ru.otus.homework.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Book;

public interface BookRepository extends MongoRepository<Book, String> {

  List<Book> findAllByAuthorId(String id);

  List<Book> findAllByGenreId(String id);
}
