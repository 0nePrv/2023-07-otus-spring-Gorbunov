package ru.otus.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionReaderServiceImplTest {

    @Test
    @DisplayName("Testing getQuestions method")
    public void testGetQuestions() {
        QuestionDao mockQuestionDao = Mockito.mock(QuestionDao.class);
        List<Question> expectedQuestions = Collections.singletonList(
                new Question("Test Question", Collections.emptyList()));
        Mockito.when(mockQuestionDao.readAllQuestions()).thenReturn(expectedQuestions);

        QuestionReaderService questionReaderService = new QuestionReaderServiceImpl(mockQuestionDao);
        List<Question> actualQuestions = questionReaderService.getQuestions();

        assertEquals(expectedQuestions, actualQuestions);
    }
}
