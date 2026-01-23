package io.github._127_0_0_l.services;

import io.github._127_0_0_l.ports.out.HtmlProviderPort;
import org.springframework.stereotype.Service;

@Service
public class HtmlProviderService {
    private final HtmlProviderPort provider;

    public HtmlProviderService (HtmlProviderPort provider){
        this.provider = provider;
    }

    public void showHtml(){
        String url = "https://google.com";
        String html = provider.getHtml(url);
        System.err.println(html);
    }
}
