package ru.otus.homework.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.exceptions.InvalidCorrectAnswerIndexException;
import ru.otus.homework.exceptions.QuestionDataReadingException;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Question service")
@TestMethodOrder(MethodOrderer.Random.class)
public class QuestionServiceImplTest {

    @Mock
    private QuestionDao mockQuestionDao;

    @Mock
    private QuestionConverter mockConverterToString;

    @Mock
    private OutputService mockOutputService;

    @InjectMocks
    private QuestionServiceImpl questionService;

    @Test
    void should_print_questions_successfully() {
        Question question = new Question("Question Text",
                Collections.singletonList(new Answer("Answer Text", true)));
        when(mockQuestionDao.readAllQuestions()).thenReturn(Collections.singletonList(question));
        when(mockConverterToString.convert(question)).thenReturn("Converted Question");

        questionService.printQuestions();

        verify(mockQuestionDao, times(1)).readAllQuestions();
        verify(mockConverterToString, times(1)).convert(question);
        verify(mockOutputService, times(1)).outputString("Converted Question");
    }

    @Test
    void should_handle_exception_when_reading_data() {
        when(mockQuestionDao.readAllQuestions()).thenThrow(
                new QuestionDataReadingException("An error occurred while reading CSV data", new IOException()));

        questionService.printQuestions();

        verify(mockQuestionDao, times(1)).readAllQuestions();
        verify(mockOutputService, times(1))
                .outputString("An error occurred while reading CSV data");
    }

    @Test
    void should_handle_exception_invalid_correct_answer_index() {
        when(mockQuestionDao.readAllQuestions()).thenThrow(
                new InvalidCorrectAnswerIndexException("Correct answer is out of range"));

        questionService.printQuestions();

        verify(mockQuestionDao, times(1)).readAllQuestions();
        verify(mockOutputService, times(1)).outputString("Correct answer is out of range");
    }
}