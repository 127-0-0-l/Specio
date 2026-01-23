package io.github._127_0_0_l.adapters.out;

import io.github._127_0_0_l.interfaces.HttpClient;
import io.github._127_0_0_l.models.Site;
import io.github._127_0_0_l.ports.out.HtmlProviderPort;
import org.springframework.stereotype.Component;

@Component
public class HtmlProviderAdapter implements HtmlProviderPort {

    private final HttpClient httpClient;

    public HtmlProviderAdapter(HttpClient httpClient){
        this.httpClient = httpClient;
    }

    @Override
    public String getHtml(Site site) {
        return httpClient.getHttp(site.url());
    }
}
