package io.github._127_0_0_l.infra_parser.interfaces;

import io.github._127_0_0_l.core.models.ParserConfig;
import io.github._127_0_0_l.infra_parser.models.HtmlParserConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CoreMapper {
    HtmlParserConfig toHtmlParserConfig(ParserConfig config);
}
