package io.github._127_0_0_l.infra_parser.adapters.out;

import io.github._127_0_0_l.core.models.VehicleAdvert;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import io.github._127_0_0_l.infra_parser.interfaces.ParserMapper;
import io.github._127_0_0_l.infra_parser.interfaces.HtmlParser;
import io.github._127_0_0_l.core.ports.out.ParserPort;
import io.github._127_0_0_l.core.ports.out.db.ParserConfigPort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParserAdapter implements ParserPort {

    private final ParserMapper mapper;
    private final HtmlParser htmlParser;
    private final ParserConfigPort parserConfigPort;
    private final ContentSourcePort contentSourcePort;

    public ParserAdapter(
            ParserMapper mapper,
            HtmlParser htmlParser,
            ParserConfigPort parserConfigPort,
            ContentSourcePort contentSourcePort){
        this.mapper = mapper;
        this.htmlParser = htmlParser;
        this.parserConfigPort = parserConfigPort;
        this.contentSourcePort = contentSourcePort;
    }

    @Override
    public List<VehicleAdvert> parse(Long sourceId, String content) {
        var parserConfig = parserConfigPort.get(sourceId);
        var source = contentSourcePort.get(sourceId);
        if (parserConfig.isEmpty() && source.isEmpty()){
            return new ArrayList<>();
        }

        var htmlParserConfig = mapper.toHtmlParserConfig(parserConfig.get());
        String url = source.get().source();
        var adverts = htmlParser.parseVehicleAdverts(url, content, htmlParserConfig);
        return mapper.toCoreVehicleAdverts(adverts);
    }
}
