package io.github._127_0_0_l.core.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import io.github._127_0_0_l.core.models.ChatState;
import io.github._127_0_0_l.core.models.ContentSource;
import io.github._127_0_0_l.core.models.LastRecord;
import io.github._127_0_0_l.core.models.NewRecordsCountLog;
import io.github._127_0_0_l.core.ports.in.SchedulerUseCase;
import io.github._127_0_0_l.core.ports.out.ContentProviderPort;
import io.github._127_0_0_l.core.ports.out.FilterPort;
import io.github._127_0_0_l.core.ports.out.NotificationPort;
import io.github._127_0_0_l.core.ports.out.ParserPort;
import io.github._127_0_0_l.core.ports.out.db.ContentSourcePort;
import io.github._127_0_0_l.core.ports.out.db.LastRecordPort;
import io.github._127_0_0_l.core.ports.out.db.NewRecordsCountLogPort;
import io.github._127_0_0_l.core.ports.out.db.TgChatPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchNotifyService implements SchedulerUseCase {

    private final ContentProviderPort contentProvider;
    private final ParserPort parserPort;
    private final ContentSourcePort contentSourcePort;
    private final TgChatPort tgChatPort;
    private final NotificationPort notificationPort;
    private final FilterPort filterPort;
    private final LastRecordPort lastRecordPort;
    private final NewRecordsCountLogPort newRecordsCountLogPort;

    public FetchNotifyService(
            ContentProviderPort contentProvider,
            ParserPort parserPort,
            ContentSourcePort contentSourcePort,
            TgChatPort tgChatPort,
            NotificationPort notificationPort,
            FilterPort filterPort,
            LastRecordPort lastRecordPort,
            NewRecordsCountLogPort newRecordsCountLogPort){
        this.contentProvider = contentProvider;
        this.parserPort = parserPort;
        this.contentSourcePort = contentSourcePort;
        this.tgChatPort = tgChatPort;
        this.notificationPort = notificationPort;
        this.filterPort = filterPort;
        this.lastRecordPort = lastRecordPort;
        this.newRecordsCountLogPort = newRecordsCountLogPort;
    }

    @Override
    public Optional<NewRecordsCountLog> runFetchAndNotify(Long contentSourceId) {
        log.info("start getting data");
        Optional<ContentSource> osource = contentSourcePort.get(contentSourceId);
        if (osource.isEmpty()){
            log.warn("source not found");
            return Optional.empty();
        }
        ContentSource source = osource.get();

        String content = contentProvider.getContent(source.id());
        LocalDateTime executionTime = LocalDateTime.now();
        log.info("data resieved");

        var chats = tgChatPort.getByState(ChatState.NOTIFYING);
        var result = parserPort.parse(source.id(), content);

        var lastRecord = lastRecordPort.getLastRecord(source.id());
        if (lastRecord.isPresent()){
            result = filterPort.filterVehicleAdverts(result, lastRecord.get().recordIdentifier());
        }
        if (result.size() > 0){
            lastRecordPort.updateLastRecord(new LastRecord(source, result.getFirst().url()));
        }
        log.info("data parsed. number of items: " + result.size());

        if (result.size() > 0){
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

        return Optional.of(new NewRecordsCountLog(executionTime, source.id(), result.size()));
    }

    @Override
    public void saveNewRecordsCountLog(NewRecordsCountLog model) {
        newRecordsCountLogPort.create(model);
    }
     
}
