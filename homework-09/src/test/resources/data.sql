INSERT INTO author (name)
VALUES ('William Shakespeare'),
       ('Fyodor Dostoevsky'),
       ('Leo Tolstoy'),
       ('Jane Austen'),
       ('Charles Dickens'),
       ('Gabriel Garcia Marquez'),
       ('George Orwell'),
       ('Ernest Hemingway'),
       ('Henry James');

INSERT INTO genre (name)
VALUES ('Drama'),
       ('Novel'),
       ('Satire'),
       ('Adventure');

INSERT INTO book (name, author_id, genre_id)
VALUES ('Hamlet', 1, 1),
       ('Crime and Punishment', 2, 2),
       ('War and Peace', 3, 2),
       ('Pride and Prejudice', 4, 2),
       ('Oliver Twist', 5, 3),
       ('One Hundred Years of Solitude', 6, 2),
       ('1984', 7, 3),
       ('Farewell to Arms', 8, 4),
       ('The Portrait of a Lady', 9, 2);

INSERT INTO PUBLIC.COMMENT (TEXT, BOOK_ID)
VALUES ('Intriguing and thought-provoking; a must-read for anyone who loves a captivating blend of science, adventure, and suspense', 1),
       ('A spellbinding tale that weaves together mystery, romance, and the complexities of human nature', 2),
       ('An epic of Russian literature that delves deep into the human psyche, exploring the moral dilemmas of its compelling characters', 3),
       ('A timeless classic exploring the intricacies of societal expectations and the power of love', 4),
       ('A Dickensian masterpiece filled with vivid characters and a compelling narrative', 5),
       ('A literary marvel that transports readers to a world of magical realism', 6),
       ('A dystopian masterpiece that serves as a stark warning about the dangers of totalitarianism',7),
       ('Hemingway''s poignant portrayal of love and loss during wartime', 8),
       ('A rich and intricate exploration of society, identity, and personal freedom', 9);
