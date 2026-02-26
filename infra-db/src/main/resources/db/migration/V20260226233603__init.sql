CREATE TABLE IF NOT EXISTS regions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS filters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price_from INT,
    price_to INT,
    year_from INT,
    year_to INT,
    CONSTRAINT chk_filter_price CHECK (price_from <= price_to OR price_from IS NULL OR price_to IS NULL),
    CONSTRAINT chk_filter_year CHECK (year_from <= year_to OR year_from IS NULL OR year_to IS NULL)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS tg_chats (
    id BIGINT PRIMARY KEY,
    state VARCHAR(100) NOT NULL,
    filters_id BIGINT UNIQUE,
    last_message_id BIGINT,
    CONSTRAINT fk_chat_filters FOREIGN KEY (filters_id) REFERENCES filters(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS filter_regions (
    filter_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    PRIMARY KEY (filter_id, region_id),
    CONSTRAINT fk_fr_filter FOREIGN KEY (filter_id) REFERENCES filters(id) ON DELETE CASCADE,
    CONSTRAINT fk_fr_region FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS filter_cities (
    filter_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    PRIMARY KEY (filter_id, city_id),
    CONSTRAINT fk_fc_filter FOREIGN KEY (filter_id) REFERENCES filters(id) ON DELETE CASCADE,
    CONSTRAINT fk_fc_city FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE
) ENGINE=InnoDB;