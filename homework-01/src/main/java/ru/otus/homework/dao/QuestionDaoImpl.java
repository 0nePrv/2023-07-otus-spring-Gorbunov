package ru.otus.homework.dao;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import ru.otus.homework.domain.Answer;
import ru.otus.homework.domain.Question;
import ru.otus.homework.exceptions.InvalidCorrectAnswerIndexException;
import ru.otus.homework.exceptions.QuestionDataReadingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionDaoImpl implements QuestionDao {

    private static final String QUESTION_HEADER = "Question";

    private static final String ANSWERS_HEADER = "Answers";

    private static final String ANSWERS_DELIMITER = ",";

    private static final String CORRECT_ANSWER_HEADER = "Correct";

    private final String resourcePath;

    public QuestionDaoImpl(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public List<Question> readAllQuestions() {
        var schema = CsvSchema.builder().addColumns(List.of(QUESTION_HEADER, ANSWERS_HEADER, CORRECT_ANSWER_HEADER),
                CsvSchema.ColumnType.STRING).build().withHeader();
        try (var inputStream = getResourseInputStream();
             MappingIterator<Map<String, String>> iterator = new CsvMapper()
                     .readerFor(Map.class).with(schema).readValues(inputStream)) {
            if (!iterator.hasNext()) {
                throw new NullPointerException();
            }
            return iterator.readAll().stream().map(this::convertToQuestion).toList();
        } catch (IOException ex) {
            throw new QuestionDataReadingException("An error occurred while reading data", ex);
        } catch (NumberFormatException ex) {
            throw new QuestionDataReadingException("Answer index is not a number", ex);
        } catch (NullPointerException ex) {
            throw new QuestionDataReadingException("Invalid data structure", ex);
        }
    }

    private Question convertToQuestion(Map<String, String> source) {
        var questionText = source.get(QUESTION_HEADER);
        var answerTexts = source.get(ANSWERS_HEADER).split(ANSWERS_DELIMITER);
        var correctAnswerIndex = (Integer.parseInt(source.get(CORRECT_ANSWER_HEADER))) - 1;
        checkCorrectAnswerIndex(answerTexts, correctAnswerIndex);
        return makeQuestion(questionText, answerTexts, correctAnswerIndex);
    }

    private Question makeQuestion(String questionText, String[] answerTexts, int correctAnswerIndex) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < answerTexts.length; i++) {
            answers.add(new Answer(answerTexts[i], i == correctAnswerIndex));
        }
        return new Question(questionText, answers);
    }

    private InputStream getResourseInputStream() throws IOException {
        var inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Incorrect resource path");
        }
        return inputStream;
    }

    private void checkCorrectAnswerIndex(String[] answers, int correctAnswerIndex) {
        if (correctAnswerIndex >= answers.length || correctAnswerIndex < 0) {
            throw new InvalidCorrectAnswerIndexException("Correct answer is out of range");
        }
    }
}