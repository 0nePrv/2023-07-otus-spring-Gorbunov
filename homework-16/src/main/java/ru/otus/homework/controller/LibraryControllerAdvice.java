package ru.otus.homework.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.homework.exception.NotExistException;

@ControllerAdvice
public class LibraryControllerAdvice {

  @ExceptionHandler(NotExistException.class)
  public String handleNotExistException(Model model, HttpServletRequest request) {
    model.addAttribute("timestamp", LocalDateTime.now().toString());
    model.addAttribute("status", HttpStatus.NOT_FOUND.value());
    model.addAttribute("path", request.getRequestURI());
    return "error";
  }
}
