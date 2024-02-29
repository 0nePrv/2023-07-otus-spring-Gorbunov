package ru.otus.homework.processor;

import com.mongodb.lang.Nullable;
import org.springframework.lang.NonNull;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.domain.mongo.DAuthor;

public interface AuthorProcessor {

  @Nullable
  DAuthor process(EAuthor author);

  @NonNull
  String checkAndRetrieveDocumentId(Long id);
}
