package io.github._127_0_0_l.infra_db.interfaces;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.models.ContentType;
import io.github._127_0_0_l.core.models.ParserConfig;
import io.github._127_0_0_l.infra_db.entities.SelectorDataType;

@Mapper(componentModel = "spring")
public interface DBMapper {
    ContentSource toCoreContentSource(io.github._127_0_0_l.infra_db.entities.ContentSource model);
    io.github._127_0_0_l.infra_db.entities.ContentSource toDBContentSource (ContentSource model);

    ParserConfig toCoreParserConfig (io.github._127_0_0_l.infra_db.entities.ParserConfig model);
    io.github._127_0_0_l.infra_db.entities.ParserConfig toDBParserConfig (ParserConfig model);

    default ContentType toContentType(SelectorDataType model){
        if (model == null || model.getType() == null){
            return null;
        }
        return ContentType.valueOf(model.getType());
    }
    
    @Mapping(target = "type", source = "model")
    SelectorDataType toSelectorDataType(ContentType model);
}
