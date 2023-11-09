package ru.otus.homework.service.validation;

public record StringFields(String... values) {

  public static StringFields of(String... values) {
    return new StringFields(values);
  }
}
