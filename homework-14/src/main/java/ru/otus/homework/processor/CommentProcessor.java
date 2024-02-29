package ru.otus.homework.processor;

import org.springframework.lang.Nullable;
import ru.otus.homework.domain.mongo.DComment;
import ru.otus.homework.domain.relational.EComment;

public interface CommentProcessor {

  @Nullable
  DComment process(EComment comment);
}
