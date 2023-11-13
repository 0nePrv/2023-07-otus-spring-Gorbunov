package ru.otus.homework.service.validation;

public record Ids(String... values) {

  public static Ids of(String... values) {
    return new Ids(values);
  }
}
