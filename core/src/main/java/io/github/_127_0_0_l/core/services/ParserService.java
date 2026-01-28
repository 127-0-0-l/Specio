package io.github._127_0_0_l.core.services;

import io.github._127_0_0_l.core.ports.out.ParserPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParserService {
    private final ParserPort htmlParser;

    public ParserService(ParserPort parser){
        this.htmlParser = parser;
    }

    public String parseHtml(String sourceId, String html){
        return htmlParser.parse(sourceId, html).toString();
    }
}
