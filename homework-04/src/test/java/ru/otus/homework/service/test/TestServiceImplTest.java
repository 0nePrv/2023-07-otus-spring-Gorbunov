package ru.otus.homework.service.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Test service")
@SuppressWarnings("unused")
@SpringBootTest
public class TestServiceImplTest {

    private static final int TOTAL_SCORE = 3;

    private static final int PASSING_SCORE = 1;

    private static final int USER_ANSWER_INDEX = 1;

    @MockBean
    private LocalizationService localizationService;

    @MockBean
    private ConversionService conversionService;

    @MockBean
    private IOService ioService;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private TestConfigurationProvider testConfigurationProvider;

    @Autowired
    private TestService testService;

    @SpringBootConfiguration
    @Import(TestServiceImpl.class)
    static class TestServiceConfiguration {
    }

    @Test
    @DisplayName("should return correct test result")
    public void shouldReturnCorrectTestResult() {
        User user = new User("John", "Johns");
        List<Question> questionList = List.of(
                new Question("Question 1", Collections.singletonList(new Answer("Answer", true))),
                new Question("Question 2", Collections.singletonList(new Answer("Answer", false))),
                new Question("Question 3", Collections.singletonList(new Answer("Answer", true))));
        TestResult expecpedTestResult = new TestResult(TOTAL_SCORE, PASSING_SCORE, user);
        questionList.forEach(q -> expecpedTestResult.addUserAnswer(q,
                q.getAnswerList().get(USER_ANSWER_INDEX - 1).isCorrect()));

        given(testConfigurationProvider.getTotalScore()).willReturn(TOTAL_SCORE);
        given(testConfigurationProvider.getPassingScore()).willReturn(PASSING_SCORE);
        given(questionService.getQuestions()).willReturn(questionList);
        given(ioService.readStringWithPrompt(any())).willReturn("");
        given(ioService.readIntWithPrompt(any())).willReturn(USER_ANSWER_INDEX);

        TestResult actualTestResult = testService.runTest(user);

        assertThat(actualTestResult).usingRecursiveComparison().isEqualTo(expecpedTestResult);
        verify(ioService, times(TOTAL_SCORE)).readIntWithPrompt(any());
    }

    @Test
    @DisplayName("should throw exception when questions quantity is not enough")
    public void shouldThrowExceptionWhenQuestionsQuantityIsNotEnough() {
        List<Question> questionList = Collections.singletonList(new Question("",
                Collections.singletonList(new Answer("", true))));

        given(testConfigurationProvider.getTotalScore()).willReturn(TOTAL_SCORE);
        given(testConfigurationProvider.getPassingScore()).willReturn(PASSING_SCORE);
        given(questionService.getQuestions()).willReturn(questionList);

        assertThrows(InvalidTestConfigurationException.class,
                () -> testService.runTest(new User("", "")));
    }

    @Test
    @DisplayName("should throw exception when questions ratio is invalid")
    public void shouldThrowExceptionWhenQuestionsRatioIsInvalid() {
        List<Question> questionList = List.of(
                new Question("", Collections.singletonList(new Answer("", true))),
                new Question("", Collections.singletonList(new Answer("", true))),
                new Question("", Collections.singletonList(new Answer("", true))));

        given(testConfigurationProvider.getTotalScore()).willReturn(TOTAL_SCORE);
        given(testConfigurationProvider.getPassingScore()).willReturn(TOTAL_SCORE);
        given(questionService.getQuestions()).willReturn(questionList);

        assertThrows(InvalidTestConfigurationException.class,
                () -> testService.runTest(new User("", "")));
    }
}
