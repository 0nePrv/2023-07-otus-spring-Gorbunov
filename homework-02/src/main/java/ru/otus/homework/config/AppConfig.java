package ru.otus.homework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.ConversionService;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.dao.QuestionDaoImpl;
import ru.otus.homework.service.io.IOService;
import ru.otus.homework.service.io.IOServiceStreams;
import ru.otus.homework.service.test.TestService;
import ru.otus.homework.service.test.TestServiceImpl;
import ru.otus.homework.service.user.UserService;

import java.io.InputStream;
import java.io.PrintStream;

@Configuration
@PropertySource("classpath:app-config.properties")
public class AppConfig {

    @Value("${resource.path}")
    private String resourcePath;

    @Value("${test.total}")
    private int totalQuestionsNumber;

    @Value("${test.passing}")
    private int passingScoreNumber;

    @Value("${output.stream}")
    private PrintStream outputStream;

    @Value("${input.stream}")
    private InputStream inputStream;


    @Bean
    public QuestionDao questionDao() {
        return new QuestionDaoImpl(resourcePath);
    }

    @Bean
    TestService testService(QuestionDao questionDao, UserService userService,
                            ConversionService conversionService) {
        return new TestServiceImpl(questionDao, userService, conversionService,
                totalQuestionsNumber, passingScoreNumber);
    }

    @Bean
    IOService ioService() {
        return new IOServiceStreams(outputStream, inputStream);
    }
}
