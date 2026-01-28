package io.github._127_0_0_l.infra_parser.models;

import java.util.List;

public record HtmlParserConfig(
        List<Selector> selectors
) { }
