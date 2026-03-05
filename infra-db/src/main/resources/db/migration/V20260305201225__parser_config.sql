CREATE TABLE parser_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB;

ALTER TABLE selectors
    ADD parser_config_id BIGINT,
    ADD CONSTRAINT fk_parser_config FOREIGN KEY (parser_config_id)
        REFERENCES parser_configs(id) ON DELETE CASCADE