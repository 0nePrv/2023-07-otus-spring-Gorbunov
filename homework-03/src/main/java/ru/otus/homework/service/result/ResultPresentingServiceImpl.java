package ru.otus.homework.service.result;

import org.springframework.stereotype.Service;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.TestResult;
import ru.otus.homework.service.io.OutputService;
import ru.otus.homework.service.localization.LocalizationService;

import java.util.stream.Collectors;

@Service
public class ResultPresentingServiceImpl implements ResultPresentingService {

    private final OutputService outputService;

    private final LocalizationService localizationService;

    public ResultPresentingServiceImpl(OutputService outputService,
                                       LocalizationService localizationService) {
        this.outputService = outputService;
        this.localizationService = localizationService;
    }

    public void outputResult(TestResult testResult) {
        var positiveResult = localizationService.getMessage("result.introduction.positive",
                testResult.getUser().getName());
        var negativeResult = localizationService.getMessage("result.introduction.negative");
        var topic = testResult.isPassed() ? positiveResult : negativeResult;
        var score = localizationService.getMessage("result.score",
                testResult.getActualScore(), testResult.getTotalQuestionsNumber());
        var mistakes = testResult.getActualScore() == testResult.getTotalQuestionsNumber() ?
                localizationService.getMessage("result.mistakes.empty") : getMistakes(testResult);
        var farewell = localizationService.getMessage("result.farewell");
        var result = String.format("\n%s\n%s\n%s\n\n%s", topic, score, mistakes, farewell);
        outputService.outputString(result);
    }

    private String getMistakes(TestResult source) {
        var topic = localizationService.getMessage("result.mistakes.exist");
        var mistakesStream = source.getIncorrectAnswerQuestionList().stream()
                .map(q -> q.getText() + " " + q.getAnswerList().stream()
                        .filter(Answer::isCorrect).findFirst()
                        .orElse(new Answer("", false)).getText());
        return topic + "\n" + mistakesStream.collect(Collectors.joining("\n"));
    }
}
