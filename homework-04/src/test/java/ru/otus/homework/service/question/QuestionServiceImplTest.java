package ru.otus.homework.service.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("Question service")
@SpringBootTest
class QuestionServiceImplTest {

    @MockBean
    private QuestionDao questionDao;

    @Autowired
    private QuestionService questionService;

    @SpringBootConfiguration
    @Import(QuestionServiceImpl.class)
    static class QuestionServiceConfiguration {
    }

    @Test
    @DisplayName("should return correct question list")
    public void shouldReturnCorrectQuestionList() {
        List<Question> questions = List.of(
                new Question("Question1", Collections.singletonList(new Answer("1", true))),
                new Question("Question2", Collections.singletonList(new Answer("2", true))),
                new Question("Question3", Collections.singletonList(new Answer("3", true)))
        );

        given(questionDao.readAllQuestions()).willReturn(questions);

        assertEquals(questionService.getQuestions(), questions);
    }
}