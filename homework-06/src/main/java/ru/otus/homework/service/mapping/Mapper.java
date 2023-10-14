package ru.otus.homework.service.mapping;

@FunctionalInterface
public interface Mapper<M, D> {
  D map(M obj);
}
