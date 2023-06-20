CREATE TABLE dogs
(
    id              BIGSERIAL   NOT NULL PRIMARY KEY,
    type_of_animal  SMALLINT    NOT NULL,
    animal_sex      SMALLINT,
    nickname        VARCHAR(20) NOT NULL,
    birthday        DATE        NOT NULL,
    breed           SMALLINT,
    disabilities    BOOLEAN,
    description     TEXT,
    created_at      TIMESTAMP   NOT NULL,
    updated_at      TIMESTAMP,
    date_outcome    TIMESTAMP,
    photo_path      VARCHAR(255),
    id_volunteer    BIGINT,
    clients_dogs_id BIGINT
);

CREATE TABLE cats
(
    id              BIGSERIAL   NOT NULL PRIMARY KEY,
    type_of_animal  SMALLINT    NOT NULL,
    animal_sex      SMALLINT,
    nickname        VARCHAR(20) NOT NULL,
    birthday        DATE        NOT NULL,
    breed           SMALLINT,
    disabilities    BOOLEAN,
    description     TEXT,
    created_at      TIMESTAMP   NOT NULL,
    updated_at      TIMESTAMP,
    date_outcome    TIMESTAMP,
    photo_path      VARCHAR(255),
    id_volunteer    BIGINT,
    clients_cats_id BIGINT
);

CREATE TABLE clients_dogs
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    username         VARCHAR(20),
    name             VARCHAR(20),
    surname          VARCHAR(20),
    patronymic       VARCHAR(20),
    birthday         DATE,
    telephone        VARCHAR(12),
    email            VARCHAR(40),
    address          BIGINT,
    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    date_income      TIMESTAMP,
    date_outcome     TIMESTAMP,
    became_client    BOOLEAN,
    volunteer        BOOLEAN   NOT NULL,
    dog_id           BIGINT,
    first_probation  BOOLEAN   NOT NULL,
    probation_starts TIMESTAMP,
    probation_ends   TIMESTAMP,
    passed_probation BOOLEAN
);

CREATE TABLE clients_cats
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    username         VARCHAR(20),
    name             VARCHAR(20),
    surname          VARCHAR(20),
    patronymic       VARCHAR(20),
    birthday         DATE,
    telephone        VARCHAR(12),
    email            VARCHAR(40),
    address          BIGINT,
    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    date_income      TIMESTAMP,
    date_outcome     TIMESTAMP,
    became_client    BOOLEAN,
    volunteer        BOOLEAN   NOT NULL,
    cat_id           BIGINT,
    first_probation  BOOLEAN   NOT NULL,
    probation_starts TIMESTAMP,
    probation_ends   TIMESTAMP,
    passed_probation BOOLEAN
);

CREATE TABLE knowledge_base_dogs
(
    id                  BIGSERIAL    NOT NULL PRIMARY KEY,
    command             VARCHAR(30)  NOT NULL,
    command_description VARCHAR(255) NOT NULL,
    message             TEXT         NOT NULL
);

CREATE TABLE knowledge_base_cats
(
    id                  BIGSERIAL    NOT NULL PRIMARY KEY,
    command             VARCHAR(30)  NOT NULL,
    command_description VARCHAR(255) NOT NULL,
    message             TEXT         NOT NULL
);

CREATE TABLE addresses
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

CREATE TABLE photo_table_dogs
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    id_client  BIGSERIAL NOT NULL,
    id_animal  BIGSERIAL NOT NULL,
    path       VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE photo_table_cats
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    id_client  BIGSERIAL NOT NULL,
    id_animal  BIGSERIAL NOT NULL,
    path       VARCHAR(255),
    created_at TIMESTAMP NOT NULL
);

ALTER TABLE dogs
    ADD CONSTRAINT fk_dogs_clients_dogs FOREIGN KEY (id_volunteer) REFERENCES clients_dogs (id),
    ADD CONSTRAINT fk_dogs_clients_dogs_2 FOREIGN KEY (clients_dogs_id) REFERENCES clients_dogs (id);

ALTER TABLE cats
    ADD CONSTRAINT fk_cats_clients_cats FOREIGN KEY (id_volunteer) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_cats_clients_cats_2 FOREIGN KEY (clients_cats_id) REFERENCES clients_cats (id);

ALTER TABLE clients_dogs
    ADD CONSTRAINT fk_clients_dogs_addresses FOREIGN KEY (address) REFERENCES addresses (id),
    ADD CONSTRAINT fk_clients_dogs_dogs FOREIGN KEY (dog_id) REFERENCES dogs (id);

ALTER TABLE clients_cats
    ADD CONSTRAINT fk_clients_cats_addresses FOREIGN KEY (address) REFERENCES addresses (id),
    ADD CONSTRAINT fk_clients_cats_cats FOREIGN KEY (cat_id) REFERENCES cats (id);

ALTER TABLE photo_table_dogs
    ADD CONSTRAINT fk_photo_table_dogs_clients_dogs FOREIGN KEY (id_client) REFERENCES clients_dogs (id),
    ADD CONSTRAINT fk_photo_table_dogs_dogs FOREIGN KEY (id_animal) REFERENCES dogs (id);

ALTER TABLE photo_table_cats
    ADD CONSTRAINT fk_photo_table_cats_clients_cats FOREIGN KEY (id_client) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_photo_table_cats_cats FOREIGN KEY (id_animal) REFERENCES cats (id);

INSERT INTO knowledge_base_dogs (command, command_description, message)
VALUES ('/dog', 'Приветствие пользователя.', 'Это телеграмм бот приюта домашних животных. Выбери приют:'),
        ('/dog_info', 'Сообщение от раздела - Консультация нового пользователя', 'Вы выбрали приют для собак. Что бы вы хотели узнать?'),
        ('/dog_about', 'Информация о приюте.', 'Приют для собак "Дай лапу" был основан в 2015 году и является самым большим приютом в городе.'),
        ('/dog_timetable', 'Расписание работы приюта и адрес, схему проезда.', 'Приют находится по адресу: г.Уфа, ул. Мира, д.33'),
        ('/dog_admission', 'Контактные данные охраны для оформления пропуска на машину.', 'Для оформления пропуска обратитесь по телефону: +780012345678'),
        ('/dog_safety_measures', 'Общие рекомендации о технике безопасности на территории приюта.', 'На территории приюта запрещено находиться в состоянии а),лкогольного опьянения, дразнить животных…'),
        ('/dog_take', 'Сообщение от раздела - Консультация нового хозяина', 'С чем бы вы хотели ознакомиться подробнее?'),
        ('/dog_connection_rules', 'Правила знакомства с животным.', '1. Не навязывайте собаке своё общество. 2. Не мешайте животному самостоятельно исследовать новое окружение. 3. Не торопитесь ухаживать за собакой'),
        ('/dog_documents', 'Список документов, чтобы взять животное.', 'Для того чтобы приютить собаку необходимо иметь с собой паспорт.'),
        ('/dog_transportation', 'Список рекомендаций по транспортировке животного.', 'Собак перевозят в наморднике с поводком или шлейкой. Если собака небольшая, её можно возить без поводков и намордников — в переноске.'),
        ('/dog_puppy_at_home', 'Список рекомендаций по обустройству дома для щенка.', 'Место для щенка должно соответствовать определенным требованиям.'),
        ('/dog_at_home', 'Список рекомендаций по обустройству дома для взрослого животного.', 'Место для собаки должно соответствовать определенным требованиям.'),
        ('/dog_disability', 'Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)', 'Место для собаки с ограниченными возможностями должно соответствовать определенным требованиям.'),
        ('/dog_recommendations', 'Советы кинолога по первичному общению с собакой', 'Совет кинолога…'),
        ('/dog_cynologist', 'Рекомендации по проверенным кинологам для дальнейшего обращения к ним', 'Киногол Виктор тел. +78005553535, кинолог Ольга тел. +78003535555'),
        ('/dog_refusal_reasons', 'Список причин, почему могут отказать и не дать забрать животное изприюта.', 'Причины отказа: Нет собственного жилья, маленькие дети в семье.'),
        ('/dog_send_report', 'Сообщение об отчете о животном', 'Выберите раздел:'),
        ('/dog_send_photo', 'Сообщение об отсылке фото', 'Фотография загружена.'),
        ('/dog_send_ration', 'Сообщение об отсылке рациона', 'Рацион добавлен.'),
        ('/dog_send_feeling', 'Сообщение об отсылке самочувствия', 'Информация сохранена.'),
        ('/dog_send_changes', 'Сообщение об отсылке изменений', 'Информация сохранена.'),
        ('/dog_receive_contacts', 'Сообщение о записи контактных данные для связи.', 'Контакт сохранен.'),
        ('/dog_volunteer', 'Сообщение о вызове волонтера.', 'Свободный волонтер свяжется с вами в ближайшее время.');

INSERT INTO knowledge_base_cats (command, command_description, message)
VALUES ('/cat', 'Приветствие пользователя.', 'Это телеграмм бот приюта домашних животных. Выбери приют:'),
       ('/cat_info', 'Сообщение от раздела - Консультация нового пользователя', 'Вы выбрали приют для кошек. Что бы вы хотели узнать?'),
       ('/cat_about', 'Информация о приюте.', 'Приют для кошек "Твой милый друг" был основан в 2000 году и является самым большим приютом в городе.'),
       ('/cat_timetable', 'Расписание работы приюта и адрес, схему проезда.', 'Приют находится по адресу: г.Уфа, ул. Карамзина, д.33'),
       ('/cat_admission', 'Контактные данные охраны для оформления пропуска на машину.', 'Для оформления пропуска обратитесь по телефону: +780012345678'),
       ('/cat_safety_measures', 'Общие рекомендации о технике безопасности на территории приюта.', 'На территории приюта запрещено находиться в состоянии алкогольного опьянения, дразнить животных…'),
       ('/cat_take', 'Сообщение от раздела - Консультация нового хозяина', 'С чем бы вы хотели ознакомиться подробнее?'),
       ('/cat_connection_rules', 'Правила знакомства с животным.', '1. Не навязывайте собаке своё общество. 2. Не мешайте животному самостоятельно исследовать новое окружение. 3. Не торопитесь ухаживать за собакой'),
       ('/cat_documents', 'Список документов, чтобы взять животное.', 'Для того чтобы приютить собаку необходимо иметь с собой паспорт.'),
       ('/cat_transportation', 'Список рекомендаций по транспортировке животного.', 'Кошек перевозят в специальной переноске. В дороге переноску нельзя открывать'),
       ('/cat_kitty_at_home', 'Список рекомендаций по обустройству дома для котенка.', 'Место для котенка должно соответствовать определенным требованиям.'),
       ('/cat_at_home', 'Список рекомендаций по обустройству дома для взрослого животного.', 'Место для кошки должно соответствовать определенным требованиям.'),
       ('/cat_disability', 'Список рекомендаций по обустройству дома для животного с ограниченными возможностями (зрение, передвижение)', 'Место для кошки с ограниченными возможностями должно соответствовать определенным требованиям.'),
       ('/cat_refusal_reasons', 'Список причин, почему могут отказать и не дать забрать животное изприюта.', 'Причины отказа: Нет собственного жилья, маленькие дети в семье.'),
       ('/cat_send_report', 'Сообщение об отчете о животном', 'Выберите раздел:'),
       ('/cat_send_photo', 'Сообщение об отсылке фото', 'Фотография загружена.'),
       ('/cat_send_ration', 'Сообщение об отсылке рациона', 'Рацион добавлен.'),
       ('/cat_send_feeling', 'Сообщение об отсылке самочувствия', 'Информация сохранена.'),
       ('/cat_send_changes', 'Сообщение об отсылке изменений', 'Информация сохранена.'),
       ('/cat_receive_contacts', 'Сообщение о записи контактных данные для связи.', 'Контакт сохранен.'),
       ('/cat_volunteer', 'Сообщение о вызове волонтера.', 'Свободный волонтер свяжется с вами в ближайшее время.');

