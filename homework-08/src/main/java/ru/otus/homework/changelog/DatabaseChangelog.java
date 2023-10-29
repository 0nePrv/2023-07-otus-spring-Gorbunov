package ru.otus.homework.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
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
@SuppressWarnings("unused")
public class DatabaseChangelog {

  private List<Author> authors;

  private List<Genre> genres;

  private List<Book> books;

  @ChangeSet(order = "001", id = "dropDb", author = "0nePrv", runAlways = true)
  public void dropDb(MongoDatabase db) {
    db.drop();
  }

  @ChangeSet(order = "002", id = "insertAuthors", author = "0nePrv", runAlways = true)
  public void insertAuthors(AuthorRepository authorRepository) {
    authors = authorRepository.saveAll(List.of(
        new Author().setName("William Shakespeare"),
        new Author().setName("Fyodor Dostoevsky"),
        new Author().setName("Leo Tolstoy"),
        new Author().setName("Jane Austen"),
        new Author().setName("Charles Dickens"),
        new Author().setName("Gabriel Garcia Marquez"),
        new Author().setName("George Orwell"),
        new Author().setName("Ernest Hemingway"),
        new Author().setName("Henry James")
    ));
  }

  @ChangeSet(order = "002", id = "insertGenres", author = "0nePrv", runAlways = true)
  public void insertGenres(GenreRepository genreRepository) {
    genres = genreRepository.saveAll(List.of(
        new Genre().setName("Drama"),
        new Genre().setName("Novel"),
        new Genre().setName("Satire"),
        new Genre().setName("Adventure")
    ));
  }

  @ChangeSet(order = "003", id = "insertBooks", author = "0nePrv", runAlways = true)
  public void insertBooks(BookRepository bookRepository) {
    books = List.of(
        new Book().setName("Hamlet").setGenre(genres.get(0)).setAuthor(authors.get(0)),
        new Book().setName("Crime and Punishment").setGenre(genres.get(1))
            .setAuthor(authors.get(1)),
        new Book().setName("War and Peace").setGenre(genres.get(1)).setAuthor(authors.get(2)),
        new Book().setName("Pride and Prejudice").setGenre(genres.get(2)).setAuthor(authors.get(3)),
        new Book().setName("Oliver Twist").setGenre(genres.get(2)).setAuthor(authors.get(4)),
        new Book().setName("One Hundred Years of Solitude").setGenre(genres.get(1))
            .setAuthor(authors.get(5)),
        new Book().setName("1984").setGenre(genres.get(3)).setAuthor(authors.get(6)),
        new Book().setName("Farewell to Arms").setGenre(genres.get(3)).setAuthor(authors.get(7)),
        new Book().setName("The Portrait of a Lady").setGenre(genres.get(1))
            .setAuthor(authors.get(8))
    );
    books = bookRepository.saveAll(books);
  }

  @ChangeSet(order = "004", id = "insertComments", author = "0nePrv", runAlways = true)
  public void insertComments(CommentRepository commentRepository) {
    commentRepository.saveAll(List.of(
        new Comment().setBook(books.get(0))
            .setText("Intriguing and thought-provoking; a must-read for anyone who loves a"
                + " captivating blend of science, adventure, and suspense"),
        new Comment().setBook(books.get(1))
            .setText("A spellbinding tale that weaves together mystery, romance, and the complexities of human nature"),
        new Comment().setBook(books.get(2))
            .setText("An epic of Russian literature that delves deep into the human psyche, "
                + "exploring the moral dilemmas of its compelling characters"),
        new Comment().setBook(books.get(3))
            .setText("A timeless classic exploring the intricacies of societal expectations and the power of love"),
        new Comment().setBook(books.get(4))
            .setText("A Dickensian masterpiece filled with vivid characters and a compelling narrative"),
        new Comment().setBook(books.get(5))
            .setText("A literary marvel that transports readers to a world of magical realism"),
        new Comment().setBook(books.get(6))
            .setText("A dystopian masterpiece that serves as a stark warning about the dangers of totalitarianism"),
        new Comment().setBook(books.get(7))
            .setText("Hemingway's poignant portrayal of love and loss during wartime"),
        new Comment().setBook(books.get(8))
            .setText("A rich and intricate exploration of society, identity, and personal freedom")
    ));
  }
}