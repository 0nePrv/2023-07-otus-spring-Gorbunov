package ru.otus.homework.service.processor;

import com.mongodb.lang.Nullable;
import org.springframework.lang.NonNull;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EGenre;

public interface GenreProcessor {

  @Nullable
  DGenre process(EGenre genre);

  @NonNull
  DGenre checkAndRetrieveGenre(Long id);
}
