package ru.otus.homework.service.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final List<Question> questionList;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao) {
        questionList = new ArrayList<>(questionDao.readAllQuestions());
        Collections.shuffle(questionList);
    }

    @Override
    public void refreshQuestions() {
        Collections.shuffle(questionList);
    }

    @Override
    public Question getQuestion(int index) {
        return questionList.get(index);
    }

    @Override
    public int getQuantity() {
        return questionList.size();
    }
}