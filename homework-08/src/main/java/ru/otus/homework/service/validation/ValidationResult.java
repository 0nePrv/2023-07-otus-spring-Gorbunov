package ru.otus.homework.service.validation;

public class ValidationResult {

  private final StringBuilder messageBuilder;

  private boolean isOk;

  public ValidationResult() {
    messageBuilder = new StringBuilder();
    this.isOk = true;
  }

  public ValidationResult reject(String message) {
    messageBuilder.append(message);
    isOk = false;
    return this;
  }

  public ValidationResult append(ValidationResult other) {
    String otherMessage = other.getMessage();
    if (!otherMessage.isEmpty()) {
      this.messageBuilder.append("\n").append(otherMessage);
    }
    this.isOk |= other.isOk();
    return this;
  }

  public String getMessage() {
    return messageBuilder.toString();
  }

  public boolean isOk() {
    return isOk;
  }
}
