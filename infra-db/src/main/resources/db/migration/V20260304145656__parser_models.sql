CREATE TABLE content_sources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    source VARCHAR(500) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE selector_data_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE selectors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    selector VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    is_multiple BOOLEAN DEFAULT FALSE,
    field_name VARCHAR(255) NOT NULL,
    selector_data_types_id BIGINT,
    
    CONSTRAINT fk_selectors_parent FOREIGN KEY (parent_id) 
        REFERENCES selectors(id) ON DELETE CASCADE,
        
    CONSTRAINT fk_selectors_type FOREIGN KEY (selector_data_types_id) 
        REFERENCES selector_data_types(id) ON DELETE RESTRICT
) ENGINE=InnoDB;