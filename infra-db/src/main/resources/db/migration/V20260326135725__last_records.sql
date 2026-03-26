CREATE TABLE last_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_source_id BIGINT NOT NULL UNIQUE,
    record_identifier VARCHAR(255),
    CONSTRAINT fk_last_records_content_sources FOREIGN KEY (content_source_id)
        REFERENCES content_sources(id) ON DELETE CASCADE
) ENGINE=InnoDB;