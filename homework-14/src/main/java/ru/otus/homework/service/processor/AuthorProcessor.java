package ru.otus.homework.service.processor;

import com.mongodb.lang.Nullable;
import org.springframework.lang.NonNull;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.domain.mongo.DAuthor;

public interface AuthorProcessor {

  @Nullable
  DAuthor process(EAuthor author);

  @NonNull
  DAuthor checkAndRetrieveAuthor(Long id);
}
