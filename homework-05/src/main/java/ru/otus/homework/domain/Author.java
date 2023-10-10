package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Author {

    @Setter
    private long id;

    private String name;

    public Author(String name) {
        this.name = name;
    }

    public Author(long id) {
        this.id = id;
    }
}
