package io.github._127_0_0_l.infra_db.interfaces;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.github._127_0_0_l.core.models.*;
import io.github._127_0_0_l.infra_db.entities.SelectorDataType;
import io.github._127_0_0_l.infra_db.repositories.CityRepository;
import io.github._127_0_0_l.infra_db.repositories.ContentSourceRepository;
import io.github._127_0_0_l.infra_db.repositories.RegionRepository;
import io.github._127_0_0_l.infra_db.repositories.TgChatRepository;

@Mapper(componentModel = "spring")
public abstract class DBMapper {

    @Autowired
    protected TgChatRepository tgChatRepository;
    @Autowired
    protected RegionRepository regionRepository;
    @Autowired
    protected CityRepository cityRepository;
    @Autowired
    protected ContentSourceRepository contentSource;

    @ObjectFactory
    public io.github._127_0_0_l.infra_db.entities.TgChat createTgChat(TgChat model) {
        return tgChatRepository.findById(model.id()).orElseGet(() -> new io.github._127_0_0_l.infra_db.entities.TgChat());
    }

    @ObjectFactory
    public io.github._127_0_0_l.infra_db.entities.Region resolveRegion(Region model) {
        return regionRepository.getReferenceById(model.id());
    }

    @ObjectFactory
    public io.github._127_0_0_l.infra_db.entities.City resolveCity(City model) {
        return cityRepository.getReferenceById(model.id());
    }

    @ObjectFactory
    public io.github._127_0_0_l.infra_db.entities.ContentSource createContentSource(ContentSource model) {
        return map(model.id());
    }

    io.github._127_0_0_l.infra_db.entities.ContentSource map(Long sourceId) {
        return contentSource.findById(sourceId).orElseGet(() -> new io.github._127_0_0_l.infra_db.entities.ContentSource());
    }

    public abstract ContentSource toCoreContentSource(io.github._127_0_0_l.infra_db.entities.ContentSource model);
    public abstract List<ContentSource> toCoreContentSources(List<io.github._127_0_0_l.infra_db.entities.ContentSource> model);
    public abstract io.github._127_0_0_l.infra_db.entities.ContentSource toDBContentSource (ContentSource model);

    public abstract ParserConfig toCoreParserConfig (io.github._127_0_0_l.infra_db.entities.ParserConfig model);
    public abstract io.github._127_0_0_l.infra_db.entities.ParserConfig toDBParserConfig (ParserConfig model);

    public ContentType toContentType(SelectorDataType model){
        if (model == null || model.getType() == null){
            return null;
        }
        return ContentType.valueOf(model.getType());
    }
    @Mapping(target = "type", source = "model")
    public abstract SelectorDataType toSelectorDataType(ContentType model);

    @Mapping(target = "id", ignore = true)
    public abstract void updateTgChat(TgChat model, @MappingTarget io.github._127_0_0_l.infra_db.entities.TgChat entity);
    public abstract TgChat toCoreTgChat(io.github._127_0_0_l.infra_db.entities.TgChat model);
    public abstract List<TgChat> toCoreTgChats(List<io.github._127_0_0_l.infra_db.entities.TgChat> model);
    public abstract io.github._127_0_0_l.infra_db.entities.TgChat toDBTgChat(TgChat model);

    public ChatState toCoreTgChatState(String model){
        if (model == null){
            return null;
        }
        return ChatState.valueOf(model);
    }

    public abstract Filters toCoreFilters (io.github._127_0_0_l.infra_db.entities.Filters model);
    public abstract io.github._127_0_0_l.infra_db.entities.Filters toDBFilters (Filters model);

    public abstract List<Region> toCoreRegions (List<io.github._127_0_0_l.infra_db.entities.Region> model);
    public abstract List<io.github._127_0_0_l.infra_db.entities.Region> toDBRegions (List<Region> model);

    public abstract List<City> toCoreCities (List<io.github._127_0_0_l.infra_db.entities.City> model);
    public abstract List<io.github._127_0_0_l.infra_db.entities.City> toDBCities (List<City> model);

    public abstract LastRecord toCoreLastRecord (io.github._127_0_0_l.infra_db.entities.LastRecord model);
    public abstract io.github._127_0_0_l.infra_db.entities.LastRecord toDBLastRecord (LastRecord model);

    public NewRecordsCountLog toCoreNewRecordsCountLog (io.github._127_0_0_l.infra_db.entities.NewRecordsCountLog model) {
        return new NewRecordsCountLog(model.getDateTime(), model.getContentSource().getId(), model.getRecordsCount());
    }
    
    public abstract List<NewRecordsCountLog> toCoreNewRecordsCountLogs (List<io.github._127_0_0_l.infra_db.entities.NewRecordsCountLog> model);
    @Mapping(source = "newRecordsCount", target = "recordsCount")
    @Mapping(source = "contentSourceId", target = "contentSource")
    public abstract io.github._127_0_0_l.infra_db.entities.NewRecordsCountLog toDBNewRecordsCountLog (NewRecordsCountLog model);
}
