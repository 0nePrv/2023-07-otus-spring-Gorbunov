package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionServiceImplTest {

    @Test
    @DisplayName("Testing getQuestions method")
    public void testGetQuestions() {
//        QuestionDao mockQuestionDao = Mockito.mock(QuestionDao.class);
//        List<Question> expectedQuestions = Collections.singletonList(
//                new Question("Test Question", Collections.emptyList()));
//        Mockito.when(mockQuestionDao.readAllQuestions()).thenReturn(expectedQuestions);
//
//        QuestionService questionService = new QuestionServiceImpl(mockQuestionDao);
//        List<Question> actualQuestions = questionService.getQuestions();
//
//        assertEquals(expectedQuestions, actualQuestions);
    }
}
