package ru.otus.homework.service.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidCorrectAnswerException;
import ru.otus.homework.exceptions.fatal.InvalidTestConfigurationException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestServiceImplTest {

    private ConversionService conversionService;
    private TestService testService;

    @BeforeEach
    public void setup() {
        conversionService = mock(ConversionService.class);
        testService = new TestServiceImpl(conversionService, 10, 7);
    }

    @Test
    public void validateAnswer_InvalidAnswerIndex_ExceptionThrown() {
        Question question = new Question("Question", Collections.singletonList(new Answer("A", true)));
        assertThrows(InvalidCorrectAnswerException.class, () -> testService.validateAnswer(question, 1));
    }

    @Test
    public void validateAnswer_ValidAnswerIndex_ReturnsCorrectness() {
        Answer correctAnswer = new Answer("Correct Answer", true);
        Answer wrongAnswer = new Answer("Wrong Answer", false);
        Question question = new Question("Question", List.of(correctAnswer, wrongAnswer));

        assertTrue(testService.validateAnswer(question, 0));
        assertFalse(testService.validateAnswer(question, 1));
    }

    @Test
    public void getQuestionRepresentation() {
        Question question = new Question("Question",
                Collections.singletonList(new Answer("Answer", true)));
        when(conversionService.convert(question, String.class)).thenReturn("Converted Question");

        String representation = testService.getQuestionRepresentation(0, question);

        assertEquals("#1 Converted Question", representation);
    }

    @Test
    public void getTestResultRepresentation() {
        TestResult testResult = new TestResult(10, 7, new User("John", "Johns"));
        when(conversionService.convert(testResult, String.class)).thenReturn("Converted Result");

        String representation = testService.getTestResultRepresentation(testResult);

        assertEquals("Converted Result", representation);
    }

    @Test
    public void getLineInExceptionFormat_Fatal() {
        String formattedLine = testService.getLineInExceptionFormat("Error message", true);

        assertEquals("Fatal error occurred: Error message", formattedLine);
    }

    @Test
    public void getLineInExceptionFormat_NonFatal() {
        String formattedLine = testService.getLineInExceptionFormat("Error message", false);

        assertEquals("An error occurred: Error message", formattedLine);
    }

    @Test
    public void validateTestConfiguration_InvalidTotalQuestions_ExceptionThrown() {
        assertThrows(InvalidTestConfigurationException.class, () -> testService.validateTestConfiguration(5));
    }

    @Test
    public void validateTestConfiguration_InvalidPassingQuestions_ExceptionThrown() {
        assertDoesNotThrow(() -> testService.validateTestConfiguration(testService.getTotalQuestionsNumber()));
    }
}
