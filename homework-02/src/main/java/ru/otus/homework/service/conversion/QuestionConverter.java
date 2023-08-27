package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

@Component
public class QuestionConverter implements Converter<Question, String> {

    @Override
    public String convert(Question question) {
        var questionRepresentationBuilder = new StringBuilder();
        questionRepresentationBuilder.append(question.getText()).append("\n");

        for (int i = 0; i < question.getAnswerList().size(); i++) {
            Answer answer = question.getAnswerList().get(i);
            questionRepresentationBuilder.append(i + 1).append(") ").append(answer.getText()).append("\n");
        }
        return questionRepresentationBuilder.toString();
    }
}