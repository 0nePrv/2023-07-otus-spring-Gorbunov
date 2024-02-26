package ru.otus.homework.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Field.Write;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comment")
public class DComment {

  @Id
  private String id;

  @Field(name = "text", write = Write.NON_NULL)
  private String text;

  @DocumentReference(collection = "book", lazy = true)
  private DBook book;
}
