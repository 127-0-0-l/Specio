ALTER TABLE parser_configs
    ADD content_source_id BIGINT NOT NULL,
    ADD CONSTRAINT fk_content_source FOREIGN KEY (content_source_id)
        REFERENCES content_sources(id) ON DELETE CASCADE