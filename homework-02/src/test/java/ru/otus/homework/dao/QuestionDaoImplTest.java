package ru.otus.homework.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("Question dao")
class QuestionDaoImplTest {

    private final List<Question> questionList;

    private final QuestionDao dao;

    public QuestionDaoImplTest() {
        dao = new QuestionDaoImpl("/questions.csv");
        this.questionList = List.of(
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
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void should_provide_correct_question_by_index(int index) {
        Question actualQuestion = dao.getQuestion(index);
        Question expectedQuestion = questionList.get(index);
        assertEquals(expectedQuestion, actualQuestion);
    }

    @Test
    void should_return_correct_question_quantity() {
        int expectedQuantity = questionList.size();
        int actualQuantity = dao.getQuantity();
        assertEquals(expectedQuantity, actualQuantity);
    }
}