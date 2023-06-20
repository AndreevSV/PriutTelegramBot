ALTER TABLE addresses RENAME TO addresses_clients_dogs;

CREATE TABLE addresses_clients_cats
(
    id       BIGSERIAL PRIMARY KEY,
    index    INTEGER      NOT NULL,
    country  VARCHAR(255) NOT NULL,
    region   VARCHAR(255),
    city     VARCHAR(255) NOT NULL,
    street   VARCHAR(255),
    house    INTEGER      NOT NULL,
    letter   char,
    building INTEGER,
    flat     INTEGER
);

INSERT INTO dogs (type_of_animal, animal_sex, nickname, birthday, breed, disabilities, description, created_at,
                  photo_path)
VALUES (1, 0, 'Sharik', '11.06.2022', 5, true, 'Хромает на одну лапку. Хороший, пушистый, с черными пятнышками',
        '2023-06-09T12:34:56', 'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 0, 'Bobik', '01.05.2023', 0, false, 'Рыженький', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 1, 'Laika', '22.06.2020', 1, false, 'Очень красивый и игручий', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 0, 'Afgan', '08.02.2019', 6, false, 'Серенький пушистый', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 0, 'Pushik', '16.12.2020', 6, true, 'Косит на один глаз. Гладкошерстный', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 1, 'Dina', '12.09.2022', 10, false, 'Спокойная, общительная', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 0, 'Koresh', '18.10.2018', 12, true, 'Порвано ушко. Очень умный', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 1, 'Daina', '30.08.2023', 13, false, 'Очень активный, забияка', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 1, 'Belka', '29.11.2020', 0, false, 'Глаза разных цветов', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg'),
       (1, 1, 'Gia', '11.04.2023', 0, false, 'Хвостик и ушки кисточками', '2023-06-09T12:34:56',
        'https://storage-api.petstory.ru/resize/1000x1000x80/81/e1/d0/81e1d0afbd5c49e7bd0819a934829c5e.jpeg');

INSERT INTO clients_cats (username, name, surname, patronymic, birthday, telephone, email, created_at,
                          date_income, became_client, volunteer, first_probation)
VALUES
    ('Sergey', 'Sergey', 'Barsukov', 'Andreevich', '11.06.2022', '+79661122266', 'barsukov@gmail.com', '2023-06-09T12:34:56',
        '2023-06-09T12:34:56', false, true, false),
    ('Valdimir', 'Valdimir', 'Ivanov', 'Vladimirovich', '11.06.2022', '+79221122266', 'ivanov@gmail.com', '2023-06-09T12:34:56',
        '2023-06-09T12:34:56', false, true, false),
    ('Victor', 'Victor', 'Parshikov', 'Pavlovich', '11.06.2022', '+79331122266', 'parshikov@gmail.com', '2023-06-09T12:34:56',
        '2023-06-09T12:34:56', false, false, false),
    ('Ivan', 'Ivan', 'Gorinov', 'Anatolivich', '11.06.2022', '+79441122266', 'gorinov@gmail.com', '2023-06-09T12:34:56',
        '2023-06-09T12:34:56', false, false, false);

INSERT INTO clients_dogs (username, name, surname, patronymic, birthday, telephone, email, created_at,
                          date_income, became_client, volunteer, first_probation)
VALUES
    ('Sergey', 'Sergey', 'Barsukov', 'Andreevich', '11.06.2022', '+79661122266', 'barsukov@gmail.com', '2023-06-09T12:34:56',
     '2023-06-09T12:34:56', false, true, false),
    ('Valdimir', 'Valdimir', 'Ivanov', 'Vladimirovich', '11.06.2022', '+79221122266', 'ivanov@gmail.com', '2023-06-09T12:34:56',
     '2023-06-09T12:34:56', false, true, false),
    ('Victor', 'Victor', 'Parshikov', 'Pavlovich', '11.06.2022', '+79331122266', 'parshikov@gmail.com', '2023-06-09T12:34:56',
     '2023-06-09T12:34:56', false, false, false),
    ('Ivan', 'Ivan', 'Gorinov', 'Anatolivich', '11.06.2022', '+79441122266', 'gorinov@gmail.com', '2023-06-09T12:34:56',
     '2023-06-09T12:34:56', false, false, false);

INSERT INTO addresses_clients_cats (index, country, region, city, street, house, letter, building, flat)
VALUES
    (400055, 'Russia', 'Rostov Region', 'Rostov', 'Novaya', 12, 'A', '1', 1),
    (400155, 'Russia', 'Moscow Region', 'Rostov', 'Znameni', 1, '', 2, 34),
    (400055, 'Russia', 'Smolensk Region', 'Rostov', 'Korchaka', 140, 'B', 1, 11),
    (400055, 'Russia', 'Moscow', 'Moscow', 'Kuznecov', 16, 'A', 3, 200),
    (400055, 'Russia', 'Krasnodar Region', 'Krasnodar', 'Igr', 1, '', 1, 33);

INSERT INTO addresses_clients_dogs (index, country, region, city, street, house, letter, building, flat)
VALUES
    (400055, 'Russia', 'Rostov Region', 'Rostov', 'Novaya', 12, 'A', '1', 1),
    (400155, 'Russia', 'Moscow Region', 'Rostov', 'Znameni', 1, '', 2, 34),
    (400055, 'Russia', 'Smolensk Region', 'Rostov', 'Korchaka', 140, 'B', 1, 11),
    (400055, 'Russia', 'Moscow', 'Moscow', 'Kuznecov', 16, 'A', 3, 200),
    (400055, 'Russia', 'Krasnodar Region', 'Krasnodar', 'Igr', 1, '', 1, 33);
