-- Deleting constraints because of mistake
ALTER TABLE flags_dogs
    DROP CONSTRAINT fk_flags_dogs_clients_dogs,
    DROP CONSTRAINT fk_flags_dogs_clients_dogs2;

-- Adding right constraints
ALTER TABLE flags_dogs
    ADD CONSTRAINT fk_flags_dogs_clients_dogs FOREIGN KEY (client_id) REFERENCES clients_dogs (id),
    ADD CONSTRAINT fk_flags_dogs_clients_dogs2 FOREIGN KEY (volunteer_id) REFERENCES clients_dogs (id);
