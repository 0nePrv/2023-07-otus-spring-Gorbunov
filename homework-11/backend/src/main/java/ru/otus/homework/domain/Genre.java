package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Field.Write;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "genre")
public class Genre {

  @Id
  private String id;

  @Field(name = "name", write = Write.NON_NULL)
  private String name;

  public Genre(String name) {
    this.name = name;
  }
}
