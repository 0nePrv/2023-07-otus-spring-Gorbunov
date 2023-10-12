package ru.otus.homework.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {

    @Setter
    private long id;

    private String name;

    public Genre(long id) {
        this.id = id;
    }

    public Genre(String name) {
        this.name = name;
    }
}
