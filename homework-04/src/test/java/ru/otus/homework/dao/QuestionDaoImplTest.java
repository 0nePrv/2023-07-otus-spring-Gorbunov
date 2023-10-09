package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.provider.ResourcePathProvider;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.BDDMockito.given;


@DisplayName("Question dao")
@SpringBootTest(classes = QuestionDaoImpl.class)
class QuestionDaoImplTest {

    @MockBean
    private ResourcePathProvider provider;

    @Autowired
    private QuestionDao dao;

    @Test
    @DisplayName("should provide correct questions")
    public void shouldProvideCorrectQuestions() {
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

        given(provider.getPath()).willReturn(Path.of("questions.csv"));

        List<Question> actualQuestionList = dao.readAllQuestions();

        assertIterableEquals(expectedQuestionList, actualQuestionList);
    }
}