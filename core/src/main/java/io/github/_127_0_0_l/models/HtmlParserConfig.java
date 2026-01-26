package io.github._127_0_0_l.models;

import java.util.Map;

public record HtmlParserConfig (
        Site site,
        Map<String, ContentType> selectors
){ }
