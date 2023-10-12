INSERT INTO AUTHORS (name)
VALUES ('William Shakespeare'),
       ('Fyodor Dostoevsky'),
       ('Leo Tolstoy'),
       ('Jane Austen'),
       ('Charles Dickens'),
       ('Gabriel Garcia Marquez'),
       ('George Orwell'),
       ('Ernest Hemingway'),
       ('Henry James');

INSERT INTO GENRES (name)
VALUES ('Drama'),
       ('Novel'),
       ('Satire'),
       ('Adventure');

INSERT INTO BOOKS (name, author_id, genre_id)
VALUES ('Hamlet', 1, 1),
       ('Crime and Punishment', 2, 2),
       ('War and Peace', 3, 2),
       ('Pride and Prejudice', 4, 2),
       ('Oliver Twist', 5, 3),
       ('One Hundred Years of Solitude', 6, 2),
       ('1984', 7, 3),
       ('Farewell to Arms', 8, 4),
       ('The Portrait of a Lady', 9, 2);
