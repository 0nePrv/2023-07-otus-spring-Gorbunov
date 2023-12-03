package ru.otus.homework.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import java.util.List;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Comment;
import ru.otus.homework.domain.Genre;
import ru.otus.homework.repository.base.AuthorRepository;
import ru.otus.homework.repository.base.BookRepository;
import ru.otus.homework.repository.base.CommentRepository;
import ru.otus.homework.repository.base.GenreRepository;

@ChangeLog
@SuppressWarnings({"unused", "deprecation"})
public class DatabaseChangelog {

  private List<Author> authors;

  private List<Genre> genres;

  private List<Book> books;

  @ChangeSet(order = "001", id = "dropDb", author = "0nePrv", runAlways = true)
  public void dropDb(MongoDatabase db) {
    SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
    db.drop().subscribe(subscriber);
    subscriber.await();
  }

  @ChangeSet(order = "002", id = "insertAuthors", author = "0nePrv", runAlways = true)
  public void insertAuthors(AuthorRepository authorRepository) {
    authors = authorRepository.saveAll(List.of(
        new Author("William Shakespeare"),
        new Author("Fyodor Dostoevsky"),
        new Author("Leo Tolstoy"),
        new Author("Jane Austen"),
        new Author("Charles Dickens"),
        new Author("Gabriel Garcia Marquez"),
        new Author("George Orwell"),
        new Author("Ernest Hemingway"),
        new Author("Henry James")
    )).collectList().block();
  }

  @ChangeSet(order = "003", id = "insertGenres", author = "0nePrv", runAlways = true)
  public void insertGenres(GenreRepository genreRepository) {
    genres = genreRepository.saveAll(List.of(
        new Genre("Drama"),
        new Genre("Novel"),
        new Genre("Satire"),
        new Genre("Adventure")
    )).collectList().block();
  }

  @ChangeSet(order = "004", id = "insertBooks", author = "0nePrv", runAlways = true)
  public void insertBooks(BookRepository bookRepository) {
    books = List.of(
        new Book("Hamlet", authors.get(0), genres.get(0)),
        new Book("Crime and Punishment", authors.get(1), genres.get(1)),
        new Book("War and Peace", authors.get(2), genres.get(1)),
        new Book("Pride and Prejudice", authors.get(3), genres.get(2)),
        new Book("Oliver Twist", authors.get(4), genres.get(2)),
        new Book("One Hundred Years of Solitude", authors.get(5), genres.get(1)),
        new Book("1984", authors.get(6), genres.get(3)),
        new Book("Farewell to Arms", authors.get(7), genres.get(3)),
        new Book("The Portrait of a Lady", authors.get(8), genres.get(1))
    );
    books = bookRepository.saveAll(books).collectList().block();
  }

  @ChangeSet(order = "005", id = "insertComments", author = "0nePrv", runAlways = true)
  public void insertComments(CommentRepository commentRepository) {
    commentRepository.saveAll(List.of(
        new Comment("Intriguing and thought-provoking; a must-read for anyone who loves a"
            + " captivating blend of science, adventure, and suspense", books.get(0)),
        new Comment("A spellbinding tale that weaves together mystery, romance, "
            + "and the complexities of human nature", books.get(1)),
        new Comment("An epic of Russian literature that delves deep into the human psyche, "
            + "exploring the moral dilemmas of its compelling characters", books.get(2)),
        new Comment("A timeless classic exploring the intricacies of societal expectations"
            + " and the power of love", books.get(3)),
        new Comment("A Dickensian masterpiece filled with vivid characters and a compelling"
            + " narrative", books.get(4)),
        new Comment("A literary marvel that transports readers to a world of magical realism",
            books.get(5)),
        new Comment("A dystopian masterpiece that serves as a stark warning about the dangers"
            + " of totalitarianism", books.get(6)),
        new Comment("Hemingway's poignant portrayal of love and loss during wartime",
            books.get(7)),
        new Comment("A rich and intricate exploration of society, identity, and personal "
            + "freedom", books.get(8))
    )).collectList().block();
  }
}