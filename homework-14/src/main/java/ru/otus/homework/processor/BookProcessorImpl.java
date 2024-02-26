package ru.otus.homework.processor;

import com.mongodb.lang.Nullable;
import jakarta.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.domain.relational.EBook;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.exception.BookNotExistException;
import ru.otus.homework.provider.CacheSizeLimitPropertyProvider;

@Service
public class BookProcessorImpl implements BookProcessor {

  private final CacheSizeLimitPropertyProvider cacheSizeLimitPropertyProvider;

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  private final AuthorProcessor authorProcessor;

  private final GenreProcessor genreProcessor;

  private final MongoOperations mongoOperations;

  public BookProcessorImpl(CacheSizeLimitPropertyProvider cacheSizeLimitPropertyProvider,
      AuthorProcessor authorProcessor, GenreProcessor genreProcessor,
      MongoOperations mongoOperations) {
    this.cacheSizeLimitPropertyProvider = cacheSizeLimitPropertyProvider;
    this.authorProcessor = authorProcessor;
    this.genreProcessor = genreProcessor;
    this.mongoOperations = mongoOperations;
  }

  @Override
  @Nullable
  public DBook process(EBook book) {
    if (isMaintained(book.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    DAuthor dAuthor = processAuthor(book.getAuthor());
    DGenre dGenre = processGenre(book.getGenre());

    DBook dBook = new DBook(documentId, book.getName(), dAuthor, dGenre);
    if (cache.size() < cacheSizeLimitPropertyProvider.getCacheSizeLimit()) {
      cache.put(book.getId(), documentId);
    }
    mongoOperations.insert(BookIdTemp.of(book.getId(), documentId));
    return dBook;
  }

  @Override
  @NonNull
  public String checkAndRetrieveDocumentId(Long id) {
    String fromCache = cache.get(id);
    if (fromCache != null) {
      return fromCache;
    }
    BookIdTemp fromDb = mongoOperations.findById(id, BookIdTemp.class);
    if (fromDb == null) {
      throw new BookNotExistException(
          "Book with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return fromDb.documentId();
  }

  @Override
  public void doCleanUp() {
    cache.clear();
  }

  @PreDestroy
  private void dropTempCollection() {
    mongoOperations.dropCollection(BookIdTemp.class);
  }

  private DAuthor processAuthor(EAuthor author) {
    DAuthor dAuthor = authorProcessor.checkAndRetrieveById(author.getId());
    if (dAuthor.getName() == null) {
      dAuthor.setName(author.getName());
    }
    return dAuthor;
  }

  private DGenre processGenre(EGenre genre) {
    DGenre dGenre = genreProcessor.checkAndRetrieveById(genre.getId());
    if (dGenre.getName() == null) {
      dGenre.setName(genre.getName());
    }
    return dGenre;
  }

  private boolean isMaintained(Long id) {
    if (cache.containsKey(id)) {
      return true;
    }
    Criteria relationalId = Criteria.where("_id").is(id);
    return mongoOperations.exists(Query.query(relationalId), BookIdTemp.class);
  }

  @Document(collection = "book_id_temp")
  private record BookIdTemp(@Id long relationalId, @Field(name = "document_id") String documentId) {

    private static BookIdTemp of(Long relationalId, String documentId) {
      return new BookIdTemp(relationalId, documentId);
    }
  }
}
