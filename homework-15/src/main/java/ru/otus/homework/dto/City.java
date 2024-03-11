package ru.otus.homework.dto;


import lombok.Getter;

@Getter
public enum City {

  LONDON("London"), MOSCOW("Moscow"), SYDNEY("Sydney"), BERLIN("Berlin"),
  WARSAW("Warsaw"), MADRID("Madrid"), WASHINGTON("Washington");

  private final String name;

  City(String name) {
    this.name = name;
  }

}