package io.github._127_0_0_l.infra_parser.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github._127_0_0_l.infra_parser.interfaces.HtmlParser;
import io.github._127_0_0_l.infra_parser.models.ContentType;
import io.github._127_0_0_l.infra_parser.models.HtmlParserConfig;
import io.github._127_0_0_l.infra_parser.models.Selector;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class HtmlParserService implements HtmlParser {
    public String parse(String url, String html, HtmlParserConfig config){
        ObjectMapper mapper = new ObjectMapper();
        Document document = Jsoup.parse(html, url);

        if (config.selectors().size() == 1 && config.selectors().getFirst().isMultiple()){
            ArrayNode array = parseArray(document, config.selectors().getFirst());
            return array.toString();
        } else {
            ObjectNode object = mapper.createObjectNode();
            fillRootNode(object, document, config.selectors());
            return object.toString();
        }
    }

    private void fillRootNode(ObjectNode root, Element element, List<Selector> selectors){
        for (var s : selectors){
            root.set(s.fieldName(), parseNode(element, s));
        }
    }

    private JsonNode parseNode(Element element, Selector selector){
        if (selector.isMultiple()){
            return parseArray(element, selector);
        } else {
            return parseObject(element, selector);
        }
    }

    private ArrayNode parseArray(Element element, Selector selector){
        ArrayNode arrayNode = new ObjectMapper().createArrayNode();
        Elements elements = element.select(selector.selector());

        for (var e : elements){
            if (selector.hasInnerSelectors()){
                ObjectNode localRoot = arrayNode.addObject();
                fillRootNode(localRoot, e, selector.innerSelectors());
            } else {
                arrayNode.add(getValue(e, selector.contentType()));
            }
        }

        return arrayNode;
    }

    private JsonNode parseObject(Element element, Selector selector){
        Element parsed = element.selectFirst(selector.selector());

        if (parsed == null)
            return new ObjectMapper().getNodeFactory().textNode("");

        if (selector.hasInnerSelectors()){
            ObjectNode localRoot = new ObjectMapper().createObjectNode();
            fillRootNode(localRoot, parsed, selector.innerSelectors());
            return localRoot;
        } else {
            return new ObjectMapper().getNodeFactory().textNode(getValue(parsed, selector.contentType()));
        }
    }

    private String getValue(Element element, ContentType contentType){
        return switch (contentType) {
            case ContentType.TEXT -> element.text();
            case ContentType.HREF -> element.attr("abs:href");
            default -> "";
        };
    }
}
