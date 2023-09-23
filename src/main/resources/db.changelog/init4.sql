CREATE TABLE chat_cats
(
    id BIGSERIAL PRIMARY KEY,
    is_chatting BOOLEAN NOT NULL,
    message_id INTEGER,
    message_text TEXT,
    owner_id BIGINT NOT NULL UNIQUE,
    volunteer_id BIGINT NOT NULL
);

ALTER TABLE chat_cats
    ADD CONSTRAINT fk_chat_cats_clients_cats FOREIGN KEY (owner_id) REFERENCES clients_cats (id),
    ADD CONSTRAINT fk_chat_cats_clients_cats_2 FOREIGN KEY (volunteer_id) REFERENCES clients_cats(id);

CREATE TABLE chat_dogs
(
    id BIGSERIAL PRIMARY KEY,
    is_chatting BOOLEAN NOT NULL,
    message_id INTEGER,
    message_text TEXT,
    owner_id BIGINT NOT NULL UNIQUE,
    volunteer_id BIGINT NOT NULL
);

ALTER TABLE chat_dogs
    ADD CONSTRAINT fk_chat_dogs_clients_dogs FOREIGN KEY (owner_id) REFERENCES clients_dogs (id),
    ADD CONSTRAINT fk_chat_dogs_clients_dogs_2 FOREIGN KEY (volunteer_id) REFERENCES clients_dogs(id);