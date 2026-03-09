package io.github._127_0_0_l.infra_db.interfaces;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github._127_0_0_l.core.models.*;
import io.github._127_0_0_l.infra_db.entities.City;
import io.github._127_0_0_l.infra_db.entities.Region;
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

    TgChat toCoreTgChat(io.github._127_0_0_l.infra_db.entities.TgChat model);
    List<TgChat> toCoreTgChats(List<io.github._127_0_0_l.infra_db.entities.TgChat> model);
    io.github._127_0_0_l.infra_db.entities.TgChat toDBTgChat(TgChat model);

    default ChatState toCoreTgChatState(String model){
        if (model == null){
            return null;
        }
        return ChatState.valueOf(model);
    }

    Filters toCoreFilters (io.github._127_0_0_l.infra_db.entities.Filters model);
    io.github._127_0_0_l.infra_db.entities.Filters toDBFilters (Filters model);

    default City toDBCity(String model){
        if (model == null || model == ""){
            return null;
        }
        return new City(model);
    }

    default String toCoreCity(City model){
        if (model == null){
            return null;
        }
        return model.getName();
    }

    default Region toDBRegion(String model){
        if (model == null || model == ""){
            return null;
        }
        return new Region(model);
    }

    default String toCoreRegion(Region model){
        if (model == null){
            return null;
        }
        return model.getName();
    }
}
