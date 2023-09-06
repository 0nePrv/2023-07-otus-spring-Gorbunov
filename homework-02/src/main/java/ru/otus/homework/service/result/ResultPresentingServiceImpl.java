package ru.otus.homework.service.result;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.service.io.OutputService;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResultPresentingServiceImpl implements ResultPresentingService {

    private final OutputService outputService;

    public ResultPresentingServiceImpl(OutputService outputService) {
        this.outputService = outputService;
    }

    public void outputResult(TestResult testResult) {
        String positiveResult = String.format("Nice job, %s\nTest passed", testResult.getUser().getName());
        String negativeResult = "Oops...\nTest failed";
        String topic = testResult.isPassed() ? positiveResult : negativeResult;
        String score = String.format("Result: %s / %s",
                testResult.getActualScore(), testResult.getTotalQuestionsNumber());
        String mistakes = testResult.getActualScore() == testResult.getTotalQuestionsNumber() ?
                "No mistakes!" : getMistakes(testResult);
        String result = String.format("\n%s\n%s\n%s\n\nBye", topic, score, mistakes);
        outputService.outputString(result);
    }

    private String getMistakes(TestResult source) {
        String topic = "Mistakes and correct answers:";
        Stream<String> mistakesStream = source.getIncorrectAnswerQuestionList().stream()
                .map(q -> q.getText() + " " + q.getAnswerList().stream()
                        .filter(Answer::isCorrect).findFirst()
                        .orElse(new Answer("", false)).getText());
        return topic + "\n" + mistakesStream.collect(Collectors.joining("\n"));
    }
}
