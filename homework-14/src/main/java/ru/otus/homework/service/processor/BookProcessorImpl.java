package ru.otus.homework.service.processor;

import com.mongodb.lang.Nullable;
import java.util.Map;
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

  private final Map<Long, String> idMap = new ConcurrentHashMap<>();

  private final AuthorProcessor authorProcessor;

  private final GenreProcessor genreProcessor;

  public BookProcessorImpl(AuthorProcessor authorProcessor, GenreProcessor genreProcessor) {
    this.authorProcessor = authorProcessor;
    this.genreProcessor = genreProcessor;
  }

  @Override
  @Nullable
  public DBook process(@NonNull EBook book) {
    if (idMap.containsKey(book.getId())) {
      return null;
    }
    String documentId = new ObjectId().toString();
    DAuthor dAuthor = processAuthor(book.getAuthor());
    DGenre dGenre = processGenre(book.getGenre());

    idMap.put(book.getId(), documentId);
    return new DBook(documentId, book.getName(), dAuthor, dGenre);
  }

  @Override
  @NonNull
  public String checkAndRetrieveDocumentId(@NonNull Long id) {
    String documentId = idMap.get(id);
    if (documentId == null) {
      throw new BookNotExistException(
          "Book with relational id: %d does not exist in temporary storage".formatted(id));
    }
    return documentId;
  }

  private DAuthor processAuthor(EAuthor author) {
    DAuthor dAuthor = authorProcessor.checkAndRetrieveAuthor(author.getId());
    if (dAuthor.getName() == null) {
      dAuthor.setName(author.getName());
    }
    return dAuthor;
  }

  private DGenre processGenre(EGenre genre) {
    DGenre dGenre = genreProcessor.checkAndRetrieveGenre(genre.getId());
    if (dGenre.getName() == null) {
      dGenre.setName(genre.getName());
    }
    return dGenre;
  }
}
