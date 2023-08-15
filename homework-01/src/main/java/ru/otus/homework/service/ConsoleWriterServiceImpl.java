package ru.otus.homework.service;

import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;

import java.util.List;

public class ConsoleWriterServiceImpl implements ConsoleWriterService {

    private final QuestionReaderService questionReaderService;

    public ConsoleWriterServiceImpl(QuestionReaderService questionReaderService) {
        this.questionReaderService = questionReaderService;
    }

    @Override
    public void printQuestions() {
        List<Question> questions = questionReaderService.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            System.out.printf("#%d: %s\n", i + 1, questions.get(i).getText());
            printAnswers(questions.get(i));
        }
    }

    private void printAnswers(Question question) {
        for (int i = 0; i < question.getAnswerList().size(); i++) {
            Answer answer = question.getAnswerList().get(i);
            System.out.printf(" %d) %s\n", i + 1, answer.getText());
        }
    }
}
