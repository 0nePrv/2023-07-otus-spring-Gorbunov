package ru.otus.homework.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {

}
