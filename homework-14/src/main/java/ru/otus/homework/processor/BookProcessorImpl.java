package ru.otus.homework.processor;

import com.mongodb.lang.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.bson.types.ObjectId;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.DAuthor;
import ru.otus.homework.domain.mongo.DBook;
import ru.otus.homework.domain.mongo.DGenre;
import ru.otus.homework.domain.relational.EAuthor;
import ru.otus.homework.domain.relational.EBook;
import ru.otus.homework.domain.relational.EGenre;
import ru.otus.homework.exception.BookNotExistException;

@Service
public class BookProcessorImpl implements BookProcessor {

  private final Map<Long, String> cache = new ConcurrentHashMap<>();

  private final AuthorProcessor authorProcessor;

  private final GenreProcessor genreProcessor;

  public BookProcessorImpl(AuthorProcessor authorProcessor, GenreProcessor genreProcessor) {
    this.authorProcessor = authorProcessor;
    this.genreProcessor = genreProcessor;
  }

  @Override
  @Nullable
  public DBook process(EBook book) {
    if (cache.containsKey(book.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    DAuthor dAuthor = processAuthor(book.getAuthor());
    DGenre dGenre = processGenre(book.getGenre());

    cache.put(book.getId(), documentId);
    return new DBook(documentId, book.getName(), dAuthor, dGenre);
  }

  @Override
  @NonNull
  public String checkAndRetrieveDocumentId(Long id) {
    Objects.requireNonNull(id);
    String fromCache = cache.get(id);
    if (fromCache == null) {
      throw new BookNotExistException(
          "Book with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return fromCache;
  }

  private DAuthor processAuthor(EAuthor author) {
    String authorId = authorProcessor.checkAndRetrieveDocumentId(author.getId());
    return new DAuthor(authorId, author.getName());
  }

  private DGenre processGenre(EGenre genre) {
    String genreId = genreProcessor.checkAndRetrieveDocumentId(genre.getId());
    return new DGenre(genreId, genre.getName());
  }
}
