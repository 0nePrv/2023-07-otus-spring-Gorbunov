DROP TABLE IF EXISTS AUTHORS;
CREATE TABLE AUTHORS
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS GENRES;
CREATE TABLE GENRES
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS BOOKS;
CREATE TABLE BOOKS
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(50) NOT NULL,
    author_id BIGINT,
    genre_id  BIGINT,
    FOREIGN KEY (author_id) REFERENCES authors (id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id)
        ON UPDATE CASCADE ON DELETE CASCADE
);