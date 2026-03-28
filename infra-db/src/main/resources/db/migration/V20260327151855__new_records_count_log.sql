CREATE TABLE new_records_count_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_source_id BIGINT NOT NULL,
    date_time TIMESTAMP NOT NULL,
    records_count INT NOT NULL,
    CONSTRAINT fk_new_records_count_log_content_sources FOREIGN KEY (content_source_id)
        REFERENCES content_sources(id) ON DELETE CASCADE
) ENGINE=InnoDB;