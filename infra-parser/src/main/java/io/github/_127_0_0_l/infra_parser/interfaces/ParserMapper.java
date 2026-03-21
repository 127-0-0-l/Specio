package io.github._127_0_0_l.infra_parser.interfaces;

import io.github._127_0_0_l.core.models.ParserConfig;
import io.github._127_0_0_l.core.models.VehicleAdvert;
import io.github._127_0_0_l.infra_parser.models.HtmlParserConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParserMapper {
    HtmlParserConfig toHtmlParserConfig(ParserConfig config);

    List<VehicleAdvert> toCoreVehicleAdverts(List<io.github._127_0_0_l.infra_parser.models.VehicleAdvert> model);
}
