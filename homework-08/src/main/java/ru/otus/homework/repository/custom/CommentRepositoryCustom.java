package ru.otus.homework.repository.custom;

import ru.otus.homework.domain.Comment;

public interface CommentRepositoryCustom {

  Comment checkAndInsert(Comment comment);
}
