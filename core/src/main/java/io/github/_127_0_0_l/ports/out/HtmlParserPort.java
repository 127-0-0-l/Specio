package io.github._127_0_0_l.ports.out;

import java.util.Map;

public interface HtmlParserPort {
    <T> T parseHtml(String html, Map<String, String> selectorToField);
}
