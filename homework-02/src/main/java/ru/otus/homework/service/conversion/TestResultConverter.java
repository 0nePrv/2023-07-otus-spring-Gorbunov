package ru.otus.homework.service.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.TestResult;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestResultConverter implements Converter<TestResult, String> {
    @Override
    public String convert(TestResult source) {
        String topic = "\n" + (source.isPassed() ? "Nice job, " + source.getUser().getName() +
                "!\nTest passed" : "Oops...\nTest failed");
        String score = "\nResult: " + source.getActualScore() + " / " + source.getTotalQuestionsNumber();
        String mistakes = convertMistakes(source);
        return topic + score +
                (source.getActualScore() == source.getTotalQuestionsNumber() ? "\nNo mistakes!\n" : mistakes)
                 + "\nBye";
    }

    private String convertMistakes(TestResult source) {
        return "\nMistakes and correct answers:\n" +
                source.getTestMap().entrySet().stream()
                        .filter(e -> e.getValue().equals(false))
                        .map(Map.Entry::getKey)
                        .map(q -> q.getText() + " " +
                                q.getAnswerList().stream().filter(Answer::isCorrect)
                                        .findFirst().orElse(new Answer("", false))
                                        .getText())
                        .collect(Collectors.joining("\n")) + "\n";
    }
}
