package ru.otus.homework.processor;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.otus.homework.domain.relational.EBook;
import ru.otus.homework.domain.mongo.DBook;


public interface BookProcessor {

  @Nullable
  DBook process(EBook book);

  @NonNull
  String checkAndRetrieveDocumentId(Long id);

  @SuppressWarnings("unused")
  void doCleanUp();
}
