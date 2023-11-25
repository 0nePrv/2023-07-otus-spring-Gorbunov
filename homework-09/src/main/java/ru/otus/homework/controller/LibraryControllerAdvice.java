package ru.otus.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.homework.exception.NotExistException;

@ControllerAdvice
public class LibraryControllerAdvice {

  @ExceptionHandler(NotExistException.class)
  public String handleException(NotExistException exception, Model model) {
    String errorText = "Error occurred: requested object not found";
    int errorCode = HttpStatus.NOT_FOUND.value();
    model.addAttribute("errorText", errorText);
    model.addAttribute("cause", exception.getMessage());
    model.addAttribute("errorCode", "HTTP Status Code: " + errorCode);
    return "error";
  }
}
