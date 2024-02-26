package ru.otus.homework.processor;

import com.mongodb.lang.Nullable;
import org.springframework.lang.NonNull;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.domain.mongo.DGenre;

public interface GenreProcessor {

  @Nullable
  DGenre process(EGenre genre);

  @NonNull
  DGenre checkAndRetrieveById(Long id);

  @SuppressWarnings("unused")
  void doCleanUp();
}
