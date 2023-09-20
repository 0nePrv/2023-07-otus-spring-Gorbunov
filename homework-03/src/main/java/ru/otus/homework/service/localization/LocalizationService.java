package ru.otus.homework.service.localization;

public interface LocalizationService {
    String getMessage(String key, Object ...args);
}
