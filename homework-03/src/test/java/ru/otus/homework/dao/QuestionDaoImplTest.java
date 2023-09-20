package ru.otus.homework.dao;

import org.junit.jupiter.api.*;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.provider.ResourcePathProvider;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Question dao")
class QuestionDaoImplTest {

    private static final String RESOURCE_PATH = "/questions.csv";

    private QuestionDao dao;

    private ResourcePathProvider pathProvider;

    @BeforeEach
    public void setUp() {
        pathProvider = mock(ResourcePathProvider.class);
        dao = new QuestionDaoImpl(pathProvider);
    }

    @Test
    public void should_provide_correct_questions() {
        List<Question> expectedQuestionList = List.of(
                new Question("How many planets are there in the Solar System?",
                        List.of(new Answer("7", false),
                                new Answer("8", false),
                                new Answer("9", true)
                        )),
                new Question("What is the highest mountain on Earth?",
                        List.of(new Answer("Everest", true),
                                new Answer("Alps", false),
                                new Answer("Kilimanjaro", false)
                        )),
                new Question("What is the tallest land animal in the world?",
                        List.of(new Answer("Kangaroo", false),
                                new Answer("Giraffe", true),
                                new Answer("Elephant", false)
                        ))
        );

        when(pathProvider.getPath()).thenReturn(Path.of(RESOURCE_PATH));

        List<Question> actualQuestionList = dao.readAllQuestions();

        assertIterableEquals(expectedQuestionList, actualQuestionList);
    }
}