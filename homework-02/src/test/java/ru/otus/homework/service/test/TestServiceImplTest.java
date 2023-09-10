package ru.otus.homework.service.test;

import org.junit.jupiter.api.*;
import org.springframework.core.convert.ConversionService;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.question.QuestionService;
import ru.otus.homework.service.testing.TestService;
import ru.otus.homework.service.testing.TestServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test service")
public class TestServiceImplTest {

    private IOService ioService;

    private QuestionService questionService;

    private TestService testService;

    @BeforeEach
    public void setup() {
        ConversionService conversionService = mock(ConversionService.class);
        questionService = mock(QuestionService.class);
        ioService = mock(IOService.class);
        testService = new TestServiceImpl(conversionService, ioService, questionService, 3, 1);
    }

    @Test
    public void should_return_correct_test_result() {
        User user = new User("John", "Johns");
        List<Question> questionList = List.of(
                new Question("Question 1", Collections.singletonList(new Answer("Answer", true))),
                new Question("Question 2", Collections.singletonList(new Answer("Answer", false))),
                new Question("Question 3", Collections.singletonList(new Answer("Answer", true))));

        when(questionService.getQuestions()).thenReturn(questionList);
        when(ioService.readStringWithPrompt(any())).thenReturn("");
        when(ioService.readIntWithPrompt(any())).thenReturn(1);

        TestResult result = testService.runTest(user);

        assertEquals(user, result.getUser());
        assertEquals(result.getActualScore(), 2);
        verify(ioService, atLeastOnce()).readStringWithPrompt(any());
        verify(ioService, times(3)).readIntWithPrompt(any());
    }

    @Test
    public void should_throw_exception_when_questions_quantity_is_not_enough() {
        when(questionService.getQuestions()).thenReturn(Collections.emptyList());

        assertThrows(InvalidTestConfigurationException.class, () -> testService.runTest(null));
    }
}
