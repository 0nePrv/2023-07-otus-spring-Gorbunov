package ru.otus.homework.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Field.Write;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "book")
public class DBook {

  @Id
  private String id;

  @Field(name = "name", write = Write.NON_NULL)
  private String name;

  @Field(name = "author", write = Write.NON_NULL)
  private DAuthor author;

  @Field(name = "genre", write = Write.NON_NULL)
  private DGenre genre;
}
