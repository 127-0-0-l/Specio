package io.github._127_0_0_l.infra_parser.interfaces;

import io.github._127_0_0_l.infra_parser.models.HtmlParserConfig;
import io.github._127_0_0_l.infra_parser.models.VehicleAdvert;

import java.util.List;

public interface HtmlParser {
    String parse(String url, String html, HtmlParserConfig config);
}
