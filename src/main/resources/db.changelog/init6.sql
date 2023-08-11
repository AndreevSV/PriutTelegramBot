ALTER TABLE clients_dogs
    ADD CONSTRAINT clients_dogs_chat_id_unique UNIQUE (chat_id),
    DROP COLUMN chats_opened;

ALTER TABLE clients_cats
    ADD CONSTRAINT clients_cats_chat_id_unique UNIQUE (chat_id),
    DROP COLUMN chats_opened;

ALTER TABLE chat_cats
    DROP CONSTRAINT fk_chat_cats_clients_cats,
    DROP CONSTRAINT fk_chat_cats_clients_cats_2;

ALTER TABLE chat_dogs
    DROP CONSTRAINT fk_chat_dogs_clients_dogs,
    DROP CONSTRAINT fk_chat_dogs_clients_dogs_2;

ALTER TABLE chat_cats
    ADD CONSTRAINT fk_chat_cats_clients_cats FOREIGN KEY (owner_id) REFERENCES clients_cats (chat_id),
    ADD CONSTRAINT fk_chat_cats_clients_cats_2 FOREIGN KEY (volunteer_id) REFERENCES clients_cats(chat_id),
    DROP COLUMN message_id,
    DROP COLUMN message_text,
    DROP COLUMN answer_text;

ALTER TABLE chat_dogs
    ADD CONSTRAINT fk_chat_dogs_clients_dogs FOREIGN KEY (owner_id) REFERENCES clients_dogs (chat_id),
    ADD CONSTRAINT fk_chat_dogs_clients_dogs_2 FOREIGN KEY (volunteer_id) REFERENCES clients_dogs(chat_id),
    DROP COLUMN message_id,
    DROP COLUMN message_text,
    DROP COLUMN answer_text;


