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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
    void runTest_ValidInput_ReturnsTestResult() {
        User user = new User("John", "Johns");
        List<Question> questionList = List.of(
                new Question("Question 1",
                        List.of(new Answer("1", false),
                                new Answer("2", false),
                                new Answer("3", true)
                        )),
                new Question("Question 2",
                        List.of(new Answer("3", false),
                                new Answer("2", true),
                                new Answer("1", false)
                        )),
                new Question("Question 3",
                        List.of(new Answer("1", false),
                                new Answer("3", true),
                                new Answer("2", false)
                        ))
        );

        when(questionService.getQuestion(0)).thenReturn(questionList.get(0));
        when(questionService.getQuestion(1)).thenReturn(questionList.get(1));
        when(questionService.getQuestion(2)).thenReturn(questionList.get(2));
        when(ioService.readStringWithPrompt(any())).thenReturn("");
        when(ioService.readIntWithPrompt(any())).thenReturn(2);

        TestResult result = testService.runTest(user);

        assertEquals(user, result.getUser());
        assertEquals(result.getTotalQuestionsNumber(),3);
        assertEquals(result.getActualScore(),2);
        verify(ioService, atLeastOnce()).readStringWithPrompt(any());
        verify(ioService, times(3)).readIntWithPrompt(any());
        verify(questionService, times(3)).getQuestion(anyInt());
    }

    @Test
    public void should_throw_exception_when_questions_quantity_is_not_enough() {
        when(questionService.getQuantity()).thenReturn(2);

        assertThrows(InvalidTestConfigurationException.class, () -> testService.checkTestConfiguration());
        verify(questionService).getQuantity();
    }
}
