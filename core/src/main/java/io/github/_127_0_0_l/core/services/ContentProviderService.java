package io.github._127_0_0_l.core.services;

import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.ports.out.ContentProviderPort;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class ContentProviderService {
    private final ContentProviderPort contentProvider;
    private final ParserService parserService;
    private final ContentSourcePort contentSource;

    public ContentProviderService(
            ContentProviderPort contentProvider,
            ParserService parserService,
            ContentSourcePort contentSource){
        this.contentProvider = contentProvider;
        this.parserService = parserService;
        this.contentSource = contentSource;
    }

    public String getContent(ContentSource source){
        return contentProvider.getContent(source.id());
    }

    public void showContent(){
        ContentSource source = contentSource.get(Long.valueOf(1));
        String content = contentProvider.getContent(source.id());
        //PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        //out.println(content);
        var result = parserService.parse(source.id(), content);
        for (var item : result){
            System.out.println(item);
        }
    }
}
