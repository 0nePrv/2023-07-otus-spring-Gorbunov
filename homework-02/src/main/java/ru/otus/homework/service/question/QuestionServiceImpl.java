package ru.otus.homework.service.question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.homework.dao.QuestionDao;
import ru.otus.homework.domain.Question;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public Question getQuestion(int index) {
        return questionDao.readAllQuestions().get(index);
    }

    @Override
    public int getQuantity() {
        return questionDao.readAllQuestions().size();
    }
}
