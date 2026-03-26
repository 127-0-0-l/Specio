package io.github._127_0_0_l.core.services;

import io.github._127_0_0_l.core.models.ChatState;
import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.models.LastRecord;
import io.github._127_0_0_l.core.ports.out.ContentProviderPort;
import io.github._127_0_0_l.core.ports.out.FilterPort;
import io.github._127_0_0_l.core.ports.out.NotificationPort;
import io.github._127_0_0_l.core.ports.out.ParserPort;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import io.github._127_0_0_l.core.ports.out.db.LastRecordPort;
import io.github._127_0_0_l.core.ports.out.db.TgChatPort;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ContentProviderService {
    private final ContentProviderPort contentProvider;
    private final ParserPort parserPort;
    private final ContentSourcePort contentSourcePort;
    private final TgChatPort tgChatPort;
    private final NotificationPort notificationPort;
    private final FilterPort filterPort;
    private final LastRecordPort lastRecordPort;

    public ContentProviderService(
            ContentProviderPort contentProvider,
            ParserPort parserPort,
            ContentSourcePort contentSourcePort,
            TgChatPort tgChatPort,
            NotificationPort notificationPort,
            FilterPort filterPort,
            LastRecordPort lastRecordPort){
        this.contentProvider = contentProvider;
        this.parserPort = parserPort;
        this.contentSourcePort = contentSourcePort;
        this.tgChatPort = tgChatPort;
        this.notificationPort = notificationPort;
        this.filterPort = filterPort;
        this.lastRecordPort = lastRecordPort;
    }

    public String getContent(ContentSource source){
        return contentProvider.getContent(source.id());
    }

    public void showContent(){
        log.info("start getting data");
        List<ContentSource> sources = contentSourcePort.getAll();
        if (sources.isEmpty()){
            log.warn("source not found");
            return;
        }

        for (var source : sources){
            String content = contentProvider.getContent(source.id());
            log.info("data resieved");
            //PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
            //out.println(content);
    
            var chats = tgChatPort.getByState(ChatState.NOTIFYING);
            var result = parserPort.parse(source.id(), content);

            var lastRecord = lastRecordPort.getLastRecord(source.id());
            if (lastRecord.isPresent()){
                result = filterPort.filterVehicleAdverts(result, lastRecord.get().recordIdentifier());
            }
            lastRecordPort.updateLastRecord(new LastRecord(source, result.getFirst().url()));
            log.info("data parsed. number of items: " + result.size());
    
            for (var chat : chats){
                var filtered = filterPort.filterVehicleAdverts(result, chat.filters());
                log.info(filtered.size() + " filtered items for chat " + chat.id());
                for (var item : filtered){
                    try {
                        notificationPort.notify(chat.id(), item);
                        TimeUnit.SECONDS.sleep(1);
                    } catch (Exception e) {
                        log.error("", e.getMessage());
                    }
                }
            }
        }
    }
}
