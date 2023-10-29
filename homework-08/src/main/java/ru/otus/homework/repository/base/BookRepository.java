package ru.otus.homework.repository.base;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Book;
import ru.otus.homework.repository.custom.BookRepositoryCustom;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryCustom {

}
