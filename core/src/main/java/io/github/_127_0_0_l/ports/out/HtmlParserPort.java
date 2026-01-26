package io.github._127_0_0_l.ports.out;

import io.github._127_0_0_l.models.HtmlParserConfig;

import java.util.Map;

public interface HtmlParserPort {
    <T> T parseHtml(String html, HtmlParserConfig htmlParserConfig);
}
