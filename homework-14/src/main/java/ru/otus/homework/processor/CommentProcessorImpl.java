package ru.otus.homework.processor;

import jakarta.annotation.PreDestroy;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.relational.EComment;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.mongo.DComment;

@Service
public class CommentProcessorImpl implements CommentProcessor {

  private final MongoOperations mongoOperations;

  private final BookProcessor bookProcessor;

  public CommentProcessorImpl(MongoOperations mongoOperations, BookProcessor bookProcessor) {
    this.mongoOperations = mongoOperations;
    this.bookProcessor = bookProcessor;
  }

  @Override
  @Nullable
  public synchronized DComment process(EComment comment) {
    if (isMaintained(comment.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    String bookId = bookProcessor.checkAndRetrieveDocumentId(comment.getBook().getId());
    DBook dBook = new DBook().setId(bookId);

    DComment dComment = new DComment(documentId, comment.getText(), dBook);
    mongoOperations.insert(CommentIdTemp.of(comment.getId(), documentId));
    return dComment;
  }

  @PreDestroy
  private void dropTempCollection() {
    mongoOperations.dropCollection(CommentIdTemp.class);
  }

  private boolean isMaintained(Long id) {
    Criteria relationalId = Criteria.where("_id").is(id);
    return mongoOperations.exists(Query.query(relationalId), CommentIdTemp.class);
  }

  @Document(collection = "comment_id_temp")
  private record CommentIdTemp(@Id long relationalId,
                               @Field(name = "document_id") String documentId) {

    private static CommentIdTemp of(Long relationalId, String documentId) {
      return new CommentIdTemp(relationalId, documentId);
    }
  }
}
