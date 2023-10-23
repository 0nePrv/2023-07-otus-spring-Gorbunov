package ru.otus.homework.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Setter
@Getter
@Accessors(chain = true)
@Document(collection = "authors")
public class Author {

  @MongoId(FieldType.OBJECT_ID)
  private String id;

  @Field
  private String name;
}
