package io.github._127_0_0_l.services;

import io.github._127_0_0_l.models.HtmlParserConfig;
import io.github._127_0_0_l.ports.out.HtmlParserPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HtmlParserService {
    private final HtmlParserPort htmlParser;

    public HtmlParserService(HtmlParserPort htmlParser){
        this.htmlParser = htmlParser;
    }

    public String parseHtml(String html, HtmlParserConfig htmlParserConfig){
        return htmlParser.parseHtml(html, htmlParserConfig);
    }
}
