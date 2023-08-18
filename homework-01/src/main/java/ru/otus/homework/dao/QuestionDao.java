package ru.otus.homework.dao;

import java.util.List;
import java.util.Map;

public interface QuestionDao {
    List<Map<String, String>> readAllQuestions();
}
