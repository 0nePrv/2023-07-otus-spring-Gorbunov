package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "author")
public class Author {

  @Id
  private String id;

  @Field
  private String name;

  public Author(String name) {
    this.name = name;
  }
}
