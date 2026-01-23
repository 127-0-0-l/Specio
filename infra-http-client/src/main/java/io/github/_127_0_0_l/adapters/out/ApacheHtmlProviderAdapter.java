package io.github._127_0_0_l.adapters.out;

import io.github._127_0_0_l.ports.out.HtmlProviderPort;
import org.springframework.stereotype.Component;

@Component
public class ApacheHtmlProviderAdapter implements HtmlProviderPort {

    @Override
    public String getHtml(String url) {
        return String.format("<html>%s<html>", url);
    }
}
