package io.github._127_0_0_l.infra_parser.adapters.out;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import io.github._127_0_0_l.infra_parser.interfaces.CoreMapper;
import io.github._127_0_0_l.infra_parser.interfaces.HtmlParser;
import io.github._127_0_0_l.core.models.VehicleAdvert;
import io.github._127_0_0_l.core.ports.out.ParserPort;
import io.github._127_0_0_l.core.ports.out.db.ParserConfigPort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ParserAdapter implements ParserPort {

    private final CoreMapper coreMapper;
    private final HtmlParser htmlParser;
    private final ParserConfigPort parserConfigPort;
    private final ContentSourcePort contentSourcePort;

    public ParserAdapter(
            CoreMapper coreMapper,
            HtmlParser htmlParser,
            ParserConfigPort parserConfigPort,
            ContentSourcePort contentSourcePort){
        this.coreMapper = coreMapper;
        this.htmlParser = htmlParser;
        this.parserConfigPort = parserConfigPort;
        this.contentSourcePort = contentSourcePort;
    }

    @Override
    public List<VehicleAdvert> parse(String sourceId, String content) {
        var parserConfig = coreMapper.toHtmlParserConfig(parserConfigPort.get(sourceId));
        String url = contentSourcePort.get(sourceId).source();
        String json = htmlParser.parse(url, content, parserConfig);
        return toVehicleAdvert(json);
    }

    private List<VehicleAdvert> toVehicleAdvert(String json){
        ObjectMapper mapper = new ObjectMapper();
        VehicleAdvert[] array;

        try {
            array = mapper.readValue(json, VehicleAdvert[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return Arrays.asList(array);
    }
}
