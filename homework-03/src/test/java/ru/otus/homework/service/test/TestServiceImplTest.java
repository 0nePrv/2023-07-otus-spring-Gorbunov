package ru.otus.homework.service.test;

import org.junit.jupiter.api.*;
import org.springframework.core.convert.ConversionService;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.domain.User;
import ru.otus.homework.exceptions.InvalidTestConfigurationException;
import ru.otus.homework.provider.TestConfigurationProvider;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.localization.LocalizationService;
import ru.otus.homework.service.question.QuestionService;
import ru.otus.homework.service.testing.TestService;
import ru.otus.homework.service.testing.TestServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test service")
public class TestServiceImplTest {

    private static final int TOTAL_SCORE = 3;

    private static final int PASSING_SCORE = 1;

    private static final int USER_ANSWER_INDEX = 1;

    private IOService ioService;

    private QuestionService questionService;

    private TestService testService;

    private TestConfigurationProvider testConfigurationProvider;

    @BeforeEach
    public void setup() {
        LocalizationService localizationService = mock(LocalizationService.class);
        ConversionService conversionService = mock(ConversionService.class);
        testConfigurationProvider = mock(TestConfigurationProvider.class);
        questionService = mock(QuestionService.class);
        ioService = mock(IOService.class);

        testService = new TestServiceImpl(conversionService, ioService, questionService,
                localizationService, testConfigurationProvider);
    }

    @Test
    public void should_return_correct_test_result() {
        User user = new User("John", "Johns");
        List<Question> questionList = List.of(
                new Question("Question 1", Collections.singletonList(new Answer("Answer", true))),
                new Question("Question 2", Collections.singletonList(new Answer("Answer", false))),
                new Question("Question 3", Collections.singletonList(new Answer("Answer", true))));
        TestResult expecpedTestResult = new TestResult(TOTAL_SCORE, PASSING_SCORE, user);
        questionList.forEach(q -> expecpedTestResult.addUserAnswer(q,
                q.getAnswerList().get(USER_ANSWER_INDEX - 1).isCorrect()));

        when(testConfigurationProvider.getTotalScore()).thenReturn(TOTAL_SCORE);
        when(testConfigurationProvider.getPassingScore()).thenReturn(PASSING_SCORE);
        when(questionService.getQuestions()).thenReturn(questionList);
        when(ioService.readStringWithPrompt(any())).thenReturn("");
        when(ioService.readIntWithPrompt(any())).thenReturn(USER_ANSWER_INDEX);

        TestResult actualTestResult = testService.runTest(user);

        assertThat(actualTestResult).usingRecursiveComparison().isEqualTo(expecpedTestResult);
        verify(ioService, times(TOTAL_SCORE)).readIntWithPrompt(any());
    }

    @Test
    public void should_throw_exception_when_questions_quantity_is_not_enough() {
        List<Question> questionList = Collections.singletonList(new Question("",
                Collections.singletonList(new Answer("", true))));

        when(testConfigurationProvider.getTotalScore()).thenReturn(TOTAL_SCORE);
        when(testConfigurationProvider.getPassingScore()).thenReturn(PASSING_SCORE);
        when(questionService.getQuestions()).thenReturn(questionList);

        assertThrows(InvalidTestConfigurationException.class,
                () -> testService.runTest(new User("", "")));
    }

    @Test
    public void should_throw_exception_when_questions_ratio_is_invalid() {
        List<Question> questionList = List.of(
                new Question("", Collections.singletonList(new Answer("", true))),
                new Question("", Collections.singletonList(new Answer("", true))),
                new Question("", Collections.singletonList(new Answer("", true))));

        when(testConfigurationProvider.getTotalScore()).thenReturn(TOTAL_SCORE);
        when(testConfigurationProvider.getPassingScore()).thenReturn(TOTAL_SCORE);
        when(questionService.getQuestions()).thenReturn(questionList);

        assertThrows(InvalidTestConfigurationException.class,
                () -> testService.runTest(new User("", "")));
    }
}
