package ru.otus.homework.repository.base;


import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.homework.domain.Author;
import ru.otus.homework.repository.custom.AuthorRepositoryCustom;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorRepositoryCustom {

}
