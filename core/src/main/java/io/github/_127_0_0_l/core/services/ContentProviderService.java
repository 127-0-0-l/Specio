package io.github._127_0_0_l.core.services;

import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.ports.out.ContentProviderPort;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ContentProviderService {
    private final ContentProviderPort contentProvider;
    private final ParserService parserService;

    public ContentProviderService(
            ContentProviderPort contentProvider,
            ParserService parserService){
        this.contentProvider = contentProvider;
        this.parserService = parserService;
    }

    public String getContent(ContentSource source){
        return contentProvider.getContent(source.name());
    }

    public void showContent(){
        ContentSource source = new ContentSource("google", "https://google.com");
        String content = contentProvider.getContent(source.name());
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        out.println(content);
        var result = parserService.parse(source.name(), content);
        for (var item : result){
            out.println(item);
        }
    }
}
