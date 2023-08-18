package ru.otus.homework.dao;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ru.otus.homework.exceptions.QuestionDataReadingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class QuestionDaoImpl implements QuestionDao {
    private final String resourcePath;

    public QuestionDaoImpl(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public List<Map<String, String>> readAllQuestions() {
        try (var inputStream = getResourseInputStream()) {
            var schema = CsvSchema.builder()
                    .addColumns(List.of("Question", "Answers", "Correct"), CsvSchema.ColumnType.STRING)
                    .build().withHeader();
            MappingIterator<Map<String, String>> iterator = new CsvMapper().readerFor(Map.class)
                    .with(schema).readValues(inputStream);
            return iterator.readAll();
        } catch (IOException ex) {
            throw new QuestionDataReadingException("An error occurred while reading CSV data", ex);
        }
    }

    private InputStream getResourseInputStream() throws IOException {
        var inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Incorrect resource path");
        }
        return inputStream;
    }
}
