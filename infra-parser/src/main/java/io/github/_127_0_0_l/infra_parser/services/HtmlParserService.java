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
    public String parse(String html, HtmlParserConfig config){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        Document document = Jsoup.parse(html);

        fillRootNode(root, document, config.selectors());

        return root.toString();
    }

    private void fillRootNode(ObjectNode root, Element element, List<Selector> selectors){
        for (var s : selectors){
            root.put(s.fieldName(), parseNode(element, s));
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

    private ObjectNode parseObject(Element element, Selector selector){
        ObjectNode localRoot = new ObjectMapper().createObjectNode();
        Element e = element.selectFirst(selector.selector());

        if (selector.hasInnerSelectors()){
            fillRootNode(localRoot, e, selector.innerSelectors());
        } else {
            localRoot.put(selector.fieldName(), parseValue(element, selector));
        }

        return localRoot;
    }

    private String parseValue(Element element, Selector selector){
        Element parsed = element.selectFirst(selector.selector());
        return getValue(parsed, selector.contentType());
    }

    private String getValue(Element element, ContentType contentType){
        switch (contentType){
            case ContentType.TEXT:
                return element.text();
            case ContentType.HREF:
                return element.attr("abs:href");
            default:
                return "";
        }
    }
}
