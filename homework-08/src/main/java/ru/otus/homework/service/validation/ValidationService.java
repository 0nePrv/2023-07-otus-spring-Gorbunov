package ru.otus.homework.service.validation;

public interface ValidationService {

  ValidationResult validate(Ids ids);

  ValidationResult validate(StringFields stringFields);

  ValidationResult validate(Ids ids, StringFields stringFields);
}
