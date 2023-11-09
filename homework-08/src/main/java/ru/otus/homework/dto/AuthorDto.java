package ru.otus.homework.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthorDto {

  private final String id;

  private final String name;
}
