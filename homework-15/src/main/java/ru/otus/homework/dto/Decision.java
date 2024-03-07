package ru.otus.homework.dto;

public record Decision(Metric metric, String value) {

  @Override
  public String toString() {
    return metric + " : " + value;
  }
}