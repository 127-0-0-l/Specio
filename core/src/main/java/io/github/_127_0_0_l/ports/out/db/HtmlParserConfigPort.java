package io.github._127_0_0_l.ports.out.db;

import io.github._127_0_0_l.models.HtmlParserConfig;

public interface HtmlParserConfigPort {
    boolean createHtmlParserConfig(HtmlParserConfig htmlParserConfig);

    boolean updateHtmlParserConfig(HtmlParserConfig htmlParserConfig);

    boolean deleteHtmlParserConfig(String siteName);

    HtmlParserConfig getHtmlParserConfig(String siteName);
}
