DELETE FROM cities;

ALTER TABLE cities
    ADD COLUMN region_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_cities_region FOREIGN KEY (region_id)
        REFERENCES regions(id) ON DELETE CASCADE