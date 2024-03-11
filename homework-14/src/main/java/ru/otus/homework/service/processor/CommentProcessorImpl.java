package ru.otus.homework.service.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.mongo.DComment;
import ru.otus.homework.domain.relational.EComment;

@Service
public class CommentProcessorImpl implements CommentProcessor {

  private final Map<Long, String> idMap = new ConcurrentHashMap<>();

  private final BookProcessor bookProcessor;

  public CommentProcessorImpl(BookProcessor bookProcessor) {
    this.bookProcessor = bookProcessor;
  }

  @Override
  @Nullable
  public DComment process(@NonNull EComment comment) {
    if (idMap.containsKey(comment.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    idMap.put(comment.getId(), documentId);

    String bookId = bookProcessor.checkAndRetrieveDocumentId(comment.getBook().getId());
    DBook dBook = new DBook().setId(bookId);
    return new DComment(documentId, comment.getText(), dBook);
  }
}
