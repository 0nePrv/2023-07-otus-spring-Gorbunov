package ru.otus.homework.exception.validation;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;

public class RequestBodyValidationException extends RuntimeException implements ErrorResponse {

  private final List<ValidationError> body;

  private final HttpStatus status;

  public RequestBodyValidationException(HttpStatus status, Class<?> clazz,
      List<FieldError> errors) {
    super(clazz.getSimpleName() + " validation error");
    this.status = status;
    this.body = errors.stream().map(ValidationError::fromFieldError).toList();
  }

  public RequestBodyValidationException(Class<?> clazz, List<FieldError> errors) {
    this(HttpStatus.BAD_REQUEST, clazz, errors);
  }

  @Override
  @NonNull
  public HttpStatusCode getStatusCode() {
    return status;
  }

  @Override
  @NonNull
  public ProblemDetail getBody() {
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, getMessage());
    detail.setProperty("errors", body);
    return detail;
  }
}
