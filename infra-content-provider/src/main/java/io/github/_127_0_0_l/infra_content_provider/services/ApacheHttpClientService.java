package io.github._127_0_0_l.infra_content_provider.services;

import io.github._127_0_0_l.infra_content_provider.interfaces.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.springframework.stereotype.Component;

@Component
public class ApacheHttpClientService implements HttpClient {
    @Override
    public String getHttp(String url) {
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .useSystemProperties()
                .build()){

            ClassicHttpRequest request = ClassicRequestBuilder.get(url)
                    .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0")
                    .setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .setHeader("Accept-Language", "en-US,en;q=0.5")
                    .setHeader("Accept-Encoding", "gzip, deflate, br, zstd")
                    .setHeader("Upgrade-Insecure-Requests", "1")
                    .setHeader("Sec-Fetch-Dest", "document")
                    .setHeader("Sec-Fetch-Mode", "navigate")
                    .setHeader("Sec-Fetch-Site", "none")
                    .setHeader("Sec-Fetch-User", "?1")
                    .setHeader("Priority", "u=0, i")
                    .setHeader("Pragma", "no-cache")
                    .setHeader("Cache-Control", "no-cache")
                    .build();

            String content = httpClient.execute(request, response -> {
                return EntityUtils.toString(response.getEntity());
            });
            return content;
        } catch (Exception e){
            System.err.println(e.getMessage());
        }

        return "failed";
    }
}
