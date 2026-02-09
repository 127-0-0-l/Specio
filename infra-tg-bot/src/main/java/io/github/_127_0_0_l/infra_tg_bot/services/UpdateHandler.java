package io.github._127_0_0_l.infra_tg_bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Service
public class UpdateHandler implements LongPollingUpdateConsumer {
    private final TelegramClient telegramClient;
    private final NotificationService notificationService;

    public UpdateHandler(TelegramClientProvider telegramClientProvider,
                         NotificationService notificationService){
        telegramClient = telegramClientProvider.getTelegramClient();
        this.notificationService = notificationService;
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessage(update.getMessage());
            }

            if (update.hasCallbackQuery()){
                handleCallbackQuery(update.getCallbackQuery());
            }
        }
    }

    private void handleMessage(Message message){
        long chatId = message.getChatId();
        String text = message.getText();
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery){
        long chatId = callbackQuery.getMessage().getChatId();
        String callback = callbackQuery.getData();
    }
}
