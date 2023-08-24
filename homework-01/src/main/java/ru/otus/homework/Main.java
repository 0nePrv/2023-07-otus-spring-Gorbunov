package ru.otus.homework;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.homework.service.QuestionService;

public class Main {
    public static void main(String[] args) {
        var context = new ClassPathXmlApplicationContext("/spring-context.xml");
        context.getBean(QuestionService.class).printQuestions();
    }
}