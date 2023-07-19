ALTER TABLE dogs
    ADD COLUMN first_probation BOOLEAN,
    ADD COLUMN second_probation BOOLEAN,
    ADD COLUMN probation_starts DATE,
    ADD COLUMN probation_ends DATE,
    ADD COLUMN passed_first_probation BOOLEAN,
    ADD COLUMN passed_second_probation BOOLEAN;

ALTER TABLE cats
    ADD COLUMN first_probation BOOLEAN,
    ADD COLUMN second_probation BOOLEAN,
    ADD COLUMN probation_starts DATE,
    ADD COLUMN probation_ends DATE,
    ADD COLUMN passed_first_probation BOOLEAN,
    ADD COLUMN passed_second_probation BOOLEAN;

ALTER TABLE clients_dogs
DROP COLUMN first_probation,
    DROP COLUMN probation_starts,
    DROP COLUMN probation_ends,
    DROP COLUMN passed_probation;

ALTER TABLE clients_cats
    DROP COLUMN first_probation,
    DROP COLUMN probation_starts,
    DROP COLUMN probation_ends,
    DROP COLUMN passed_probation;

