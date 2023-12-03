package ru.otus.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import ru.otus.homework.exception.dataConsistency.DataConsistencyException;
import ru.otus.homework.exception.notExist.NotExistException;
import ru.otus.homework.exception.validation.RequestBodyValidationException;

@RestControllerAdvice
public class LibraryControllerAdvice {

  @ExceptionHandler(NotExistException.class)
  public Mono<ErrorResponse> handleNotExist(NotExistException exception) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    return Mono.just(new ErrorResponseException(status, detail, exception));
  }

  @ExceptionHandler(DataConsistencyException.class)
  public Mono<ErrorResponse> handleDataConsistency(DataConsistencyException exception) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    return Mono.just(new ErrorResponseException(status, detail, exception));
  }

  @ExceptionHandler(RequestBodyValidationException.class)
  public Mono<ErrorResponse> handleDataValidation(RequestBodyValidationException exception) {
    return Mono.just(exception);
  }
}
