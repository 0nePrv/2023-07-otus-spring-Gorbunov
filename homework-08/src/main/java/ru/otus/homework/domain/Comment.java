package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment")
public class Comment {

  @Id
  private String id;

  @Field
  private String text;

  @DocumentReference(collection = "book", lazy = true)
  private Book book;

  public Comment(String text, Book book) {
    this.text = text;
    this.book = book;
  }
}
