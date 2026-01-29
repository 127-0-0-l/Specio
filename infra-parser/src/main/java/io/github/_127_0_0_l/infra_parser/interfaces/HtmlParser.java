package io.github._127_0_0_l.infra_parser.interfaces;

import io.github._127_0_0_l.infra_parser.models.HtmlParserConfig;

public interface HtmlParser {
    String parse(String url, String html, HtmlParserConfig config);
}
