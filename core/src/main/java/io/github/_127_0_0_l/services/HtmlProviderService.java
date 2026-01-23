package io.github._127_0_0_l.services;

import io.github._127_0_0_l.models.Site;
import io.github._127_0_0_l.ports.out.HtmlProviderPort;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class HtmlProviderService {
    private final HtmlProviderPort htmlProvider;

    public HtmlProviderService (HtmlProviderPort provider){
        this.htmlProvider = provider;
    }

    public String getHtml(Site site){
        return htmlProvider.getHtml(site);
    }

    public void showHtml(){
        Site site = new Site(0, "google", "https://google.com");
        String html = htmlProvider.getHtml(site);
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(html);
        log.info("Hello logs!!!");
    }
}
