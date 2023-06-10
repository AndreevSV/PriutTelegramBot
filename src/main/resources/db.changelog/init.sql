CREATE TABLE dogs
(
    id              BIGSERIAL   NOT NULL PRIMARY KEY,
    type_of_animal  SMALLINT    NOT NULL,
    animal_sex      SMALLINT,
    nickname        VARCHAR(20) NOT NULL,
    birthday        TIMESTAMP   NOT NULL,
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
    birthday        TIMESTAMP   NOT NULL,
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
    birthday         TIMESTAMP,
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
    probation_starts TIMESTAMP NOT NULL,
    probation_ends   TIMESTAMP NOT NULL,
    passed_probation BOOLEAN
);

CREATE TABLE clients_cats
(
    id               BIGSERIAL NOT NULL PRIMARY KEY,
    username         VARCHAR(20),
    name             VARCHAR(20),
    surname          VARCHAR(20),
    patronymic       VARCHAR(20),
    birthday         TIMESTAMP,
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
    probation_starts TIMESTAMP NOT NULL,
    probation_ends   TIMESTAMP NOT NULL,
    passed_probation BOOLEAN
);

CREATE TABLE knowledge_base_dogs
(
    id                  BIGSERIAL    NOT NULL PRIMARY KEY,
    command             VARCHAR(20)  NOT NULL,
    command_description VARCHAR(255) NOT NULL,
    message             TEXT
);

CREATE TABLE knowledge_base_cats
(
    id                  BIGSERIAL    NOT NULL PRIMARY KEY,
    command             VARCHAR(20)  NOT NULL,
    command_description VARCHAR(255) NOT NULL,
    message             TEXT
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