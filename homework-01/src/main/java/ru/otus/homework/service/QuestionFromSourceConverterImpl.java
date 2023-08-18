package ru.otus.homework.service;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.exceptions.InvalidCorrectAnswerIndexException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionFromSourceConverterImpl implements QuestionFromSourceConverter {

    private static final String QUESTION_HEADER = "Question";

    private static final String ANSWERS_HEADER = "Answers";

    private static final String ANSWERS_DELIMITER = ",";

    private static final String CORRECT_ANSWER_HEADER = "Correct";

    @Override
    public Question convert(Map<String, String> source) {
        var questionText = source.get(QUESTION_HEADER);
        var answerTexts = source.get(ANSWERS_HEADER).split(ANSWERS_DELIMITER);
        var correctAnswerIndex = (Integer.parseInt(source.get(CORRECT_ANSWER_HEADER))) - 1;
        checkCorrectAnswerIndex(answerTexts, correctAnswerIndex);
        return makeQuestion(questionText, answerTexts, correctAnswerIndex);
    }

    private Question makeQuestion(String questionText, String[] answerTexts, int correctAnswerIndex) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < answerTexts.length; i++) {
            answers.add(new Answer(answerTexts[i], i == correctAnswerIndex));
        }
        return new Question(questionText, answers);
    }

    private void checkCorrectAnswerIndex(String[] answers, int correctAnswerIndex) {
        if (correctAnswerIndex >= answers.length || correctAnswerIndex < 0) {
            throw new InvalidCorrectAnswerIndexException("Correct answer is out of range");
        }
    }
}
