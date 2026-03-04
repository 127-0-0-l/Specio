package io.github._127_0_0_l.infra_db.interfaces;

import org.mapstruct.Mapper;

import io.github._127_0_0_l.core.models.ContentSource;

@Mapper(componentModel = "spring")
public interface DBMapper {
    ContentSource toContentSource(io.github._127_0_0_l.infra_db.entities.ContentSource model);

    io.github._127_0_0_l.infra_db.entities.ContentSource toContentSource (ContentSource model);
}
