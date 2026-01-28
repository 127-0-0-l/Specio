package io.github._127_0_0_l.infra_content_provider.adapters.out;

import io.github._127_0_0_l.infra_content_provider.interfaces.HttpClient;
import io.github._127_0_0_l.core.ports.out.ContentProviderPort;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import org.springframework.stereotype.Component;

@Component
public class ContentProviderAdapter implements ContentProviderPort {

    private final HttpClient httpClient;
    private final ContentSourcePort contentSourcePort;

    public ContentProviderAdapter(
            HttpClient httpClient,
            ContentSourcePort contentSourcePort){
        this.httpClient = httpClient;
        this.contentSourcePort = contentSourcePort;
    }

    @Override
    public String getContent(String sourceId) {
        var source = contentSourcePort.get(sourceId);
        return httpClient.getHttp(source.source());
    }
}
