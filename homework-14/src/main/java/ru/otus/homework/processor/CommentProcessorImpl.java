package ru.otus.homework.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.mongo.DComment;
import ru.otus.homework.domain.relational.EComment;

@Service
public class CommentProcessorImpl implements CommentProcessor {

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  private final BookProcessor bookProcessor;

  public CommentProcessorImpl(BookProcessor bookProcessor) {
    this.bookProcessor = bookProcessor;
  }

  @Override
  @Nullable
  public DComment process(EComment comment) {
    if (cache.containsKey(comment.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    cache.put(comment.getId(), documentId);

    String bookId = bookProcessor.checkAndRetrieveDocumentId(comment.getBook().getId());
    DBook dBook = new DBook().setId(bookId);
    return new DComment(documentId, comment.getText(), dBook);
  }
}
