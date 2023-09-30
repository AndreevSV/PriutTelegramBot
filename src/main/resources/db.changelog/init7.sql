CREATE TABLE flags_cats
(
    id                   BIGSERIAL NOT NULL PRIMARY KEY,
    client_id            BIGINT    NOT NULL,
    volunteer_id         BIGINT,
    animal_id            BIGINT,
    waiting_for_photo    BOOLEAN   NOT NULL,
    waiting_for_ration   BOOLEAN   NOT NULL,
    waiting_for_feeling  BOOLEAN   NOT NULL,
    waiting_for_changes  BOOLEAN   NOT NULL,
    waiting_for_contacts BOOLEAN   NOT NULL,
    chatting             BOOLEAN   NOT NULL,
    created_at           TIMESTAMP NOT NULL,
    date                 DATE,
    updated_at           TIMESTAMP
);

CREATE TABLE flags_dogs
(
    id                   BIGSERIAL NOT NULL PRIMARY KEY,
    client_id            BIGINT    NOT NULL,
    volunteer_id         BIGINT,
    animal_id            BIGINT,
    waiting_for_photo    BOOLEAN   NOT NULL,
    waiting_for_ration   BOOLEAN   NOT NULL,
    waiting_for_feeling  BOOLEAN   NOT NULL,
    waiting_for_changes  BOOLEAN   NOT NULL,
    waiting_for_contacts BOOLEAN   NOT NULL,
    chatting             BOOLEAN   NOT NULL,
    date                 DATE,
    created_at           TIMESTAMP NOT NULL,
    updated_at           TIMESTAMP
);


ALTER TABLE flags_cats
    ADD CONSTRAINT fk_flags_cats_clients_cats FOREIGN KEY (client_id) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_flags_cats_clients_cats2 FOREIGN KEY (volunteer_id) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_flags_cats_cats FOREIGN KEY (animal_id) REFERENCES cats (id);

ALTER TABLE flags_dogs
    ADD CONSTRAINT fk_flags_dogs_clients_dogs FOREIGN KEY (client_id) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_flags_dogs_clients_dogs2 FOREIGN KEY (volunteer_id) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_flags_dogs_dogs FOREIGN KEY (animal_id) REFERENCES dogs (id);

ALTER TABLE chat_cats
    DROP COLUMN chat_id;

ALTER TABLE chat_dogs
    DROP COLUMN chat_id;

ALTER TABLE clients_cats
    ADD CONSTRAINT fk_clients_cats_clients_cats FOREIGN KEY (id) REFERENCES clients_cats (id);

ALTER TABLE clients_dogs
    ADD CONSTRAINT fk_clients_dogs_clients_dogs FOREIGN KEY (id) REFERENCES clients_dogs (id);
