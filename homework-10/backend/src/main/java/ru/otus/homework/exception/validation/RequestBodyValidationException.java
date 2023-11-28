package ru.otus.homework.exception.validation;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;

public class RequestBodyValidationException extends RuntimeException implements ErrorResponse {

  private final HttpStatus status;

  private final ProblemDetail body;

  public RequestBodyValidationException(HttpStatus status, Class<?> clazz, List<FieldError> errors) {
    super(clazz.getSimpleName() + " validation error");
    this.status = status;
    ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, super.getMessage());
    List<ValidationError> validationErrors = errors.stream()
        .map(ValidationError::fromFieldError).toList();
    detail.setProperty("errors", validationErrors);
    this.body = detail;
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
    return body;
  }
}
