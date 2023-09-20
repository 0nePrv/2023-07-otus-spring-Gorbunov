package ru.otus.homework.service.question;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Question service")
class QuestionServiceImplTest {

    private QuestionDao questionDao;

    private QuestionService questionService;

    @BeforeEach
    public void setUp() {
        questionDao = Mockito.mock(QuestionDao.class);
        questionService = new QuestionServiceImpl(questionDao);
    }

    @Test
    public void should_return_correct_question_list() {
        List<Question> questions = List.of(
                new Question("Question1", Collections.singletonList(new Answer("1", true))),
                new Question("Question2", Collections.singletonList(new Answer("2", true))),
                new Question("Question3", Collections.singletonList(new Answer("3", true)))
        );

        when(questionDao.readAllQuestions()).thenReturn(questions);

        assertEquals(questionService.getQuestions(), questions);
    }
}