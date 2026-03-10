package io.github._127_0_0_l.core.services;

import io.github._127_0_0_l.core.models.ChatState;
import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.models.Notification;
import io.github._127_0_0_l.core.ports.out.ContentProviderPort;
import io.github._127_0_0_l.core.ports.out.NotificationPort;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;

import io.github._127_0_0_l.core.ports.out.db.TgChatPort;
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
    private final TgChatPort tgChatPort;
    private final NotificationPort notificationPort;

    public ContentProviderService(
            ContentProviderPort contentProvider,
            ParserService parserService,
            ContentSourcePort contentSource,
            TgChatPort tgChatPort,
            NotificationPort notificationPort){
        this.contentProvider = contentProvider;
        this.parserService = parserService;
        this.contentSource = contentSource;
        this.tgChatPort = tgChatPort;
        this.notificationPort = notificationPort;
    }

    public String getContent(ContentSource source){
        return contentProvider.getContent(source.id());
    }

    public void showContent(){
        ContentSource source = contentSource.get(Long.valueOf(1));
        String content = contentProvider.getContent(source.id());
        //PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        //out.println(content);

        var chats = tgChatPort.getByState(ChatState.NOTIFYING);
        var result = parserService.parse(source.id(), content);

        for (var chat : chats){
            for (var item : result){
                var notification = new Notification(chat.id(), item);
                notificationPort.Notify(notification);
            }
        }
    }
}
