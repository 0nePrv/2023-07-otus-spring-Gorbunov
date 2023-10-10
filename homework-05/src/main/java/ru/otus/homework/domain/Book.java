package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Book {

  @Setter
  private long id;

  private final String name;

  private final Author author;

  private final Genre genre;
}
