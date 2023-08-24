package ru.otus.homework.service;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

public class QuestionConverterImpl implements QuestionConverter {

    private int questionNumber = 1;

    @Override
    public String convert(Question question) {
        var outputBuilder = new StringBuilder();
        outputBuilder.append("#").append(questionNumber++).append(" ")
                .append(question.getText()).append("\n");

        for (int i = 0; i < question.getAnswerList().size(); i++) {
            Answer answer = question.getAnswerList().get(i);
            outputBuilder.append(i + 1).append(") ").append(answer.getText())
                    .append(answer.isCorrect() ? " ✓" : " ❌").append("\n");
        }
        return outputBuilder.toString();
    }
}