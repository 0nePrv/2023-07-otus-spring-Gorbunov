package ru.otus.homework.service.genre;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Genre add(Genre genre) {
        return genreDao.insert(genre);
    }

    @Override
    public void update(Genre genre) {
        genreDao.update(genre);
    }

    @Override
    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    @Override
    public Genre get(long id) {
        return genreDao.getById(id);
    }

    @Override
    public void remove(long id) {
        genreDao.deleteById(id);
    }
}
