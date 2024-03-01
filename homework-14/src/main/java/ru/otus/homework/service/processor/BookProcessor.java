package ru.otus.homework.service.processor;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.relational.EBook;


public interface BookProcessor {

  @Nullable
  DBook process(EBook book);

  @NonNull
  String checkAndRetrieveDocumentId(Long id);
}
