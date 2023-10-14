package ru.otus.homework.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.dao.GenreDao;
import ru.otus.homework.domain.Genre;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    @Transactional
    public Genre add(Genre genre) {
        return genreDao.insert(genre);
    }

    @Override
    @Transactional
    public void update(Genre genre) {
        genreDao.update(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre get(long id) {
        return genreDao.getById(id);
    }

    @Override
    @Transactional
    public void remove(long id) {
        genreDao.deleteById(id);
    }
}
