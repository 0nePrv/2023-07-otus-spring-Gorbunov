package ru.otus.homework.service.test;

import org.junit.jupiter.api.*;
import org.springframework.core.convert.ConversionService;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidAnswerException;
import ru.otus.homework.exceptions.fatal.InvalidTestConfigurationException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test service")
public class TestServiceImplTest {

    private ConversionService conversionService;
    private TestService testService;

    @BeforeEach
    public void setup() {
        conversionService = mock(ConversionService.class);
        testService = new TestServiceImpl(conversionService, 10, 7);
    }

    @Test
    public void should_throw_exception_when_answer_index_is_out_of_range() {
        Question question = new Question("Question", Collections.singletonList(new Answer("A", true)));
        assertThrows(InvalidAnswerException.class, () -> testService.validateAnswer(question, 1));
    }

    @Test
    public void should_return_correctly_check_user_answer() {
        Answer correctAnswer = new Answer("Correct Answer", true);
        Answer wrongAnswer = new Answer("Wrong Answer", false);
        Question question = new Question("Question", List.of(correctAnswer, wrongAnswer));

        assertTrue(testService.validateAnswer(question, 0));
        assertFalse(testService.validateAnswer(question, 1));
    }

    @Test
    public void should_return_correct_question_representation() {
        Question question = new Question("Question",
                Collections.singletonList(new Answer("Answer", true)));
        when(conversionService.convert(question, String.class)).thenReturn("Converted Question");

        String representation = testService.getQuestionRepresentation(0, question);

        assertEquals("#1 Converted Question", representation);
    }

    @Test
    public void should_return_correct_test_result_representation() {
        TestResult testResult = new TestResult(10, 7,
                new User("John", "Johns"));
        when(conversionService.convert(testResult, String.class)).thenReturn("Converted Result");

        String representation = testService.getTestResultRepresentation(testResult);

        assertEquals("Converted Result", representation);
    }

    @Test
    public void should_return_correct_formatted_fatal_error_message() {
        String formattedLine = testService.formatException("Error message", true);

        assertEquals("Fatal error occurred: Error message", formattedLine);
    }

    @Test
    public void should_return_correct_formatted_non_fatal_error_message() {
        String formattedLine = testService.formatException("Error message", false);

        assertEquals("An error occurred: Error message", formattedLine);
    }

    @Test
    public void should_throw_an_exception_when_test_configuration_is_invalid() {
        int invalidQuantity = testService.getTotalQuestionsNumber() - 1;
        assertThrows(InvalidTestConfigurationException.class, () ->
                testService.validateTestConfiguration(invalidQuantity));
    }

    @Test
    public void should_not_throw_an_exception_when_test_configuration_is_valid() {
        int minValidQuantity = testService.getTotalQuestionsNumber();
        assertDoesNotThrow(() -> testService.validateTestConfiguration(minValidQuantity));
    }
}
