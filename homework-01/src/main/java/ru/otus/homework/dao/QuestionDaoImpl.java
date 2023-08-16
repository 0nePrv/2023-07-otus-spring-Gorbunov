package ru.otus.homework.dao;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ru.otus.homework.domain.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {
    private final String resourcePath;

    public QuestionDaoImpl(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    @Override
    public List<Question> readAllQuestions() {
        try (InputStream resourseInputStream = getResourseInputStream()) {
            CsvSchema schema = CsvSchema.builder().addColumn("Question").addArrayColumn("Answers", ",")
                    .build().withSkipFirstDataRow(true);
            MappingIterator<Question> iterator = new CsvMapper().readerFor(Question.class).with(schema)
                    .readValues(resourseInputStream);
            return iterator.readAll();
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading data", exception);
        }
    }

    public InputStream getResourseInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Incorrect resource path");
        }
        return inputStream;
    }
}
