package io.github._127_0_0_l.infra_tg_bot.services;

import io.github._127_0_0_l.infra_tg_bot.models.ChatInlineButton;
import io.github._127_0_0_l.infra_tg_bot.models.ChatKeyboardButton;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Queue;

@Service
public class NotificationService {
    private final TelegramClient telegramClient;

    public NotificationService(TelegramClientProvider telegramClientProvider){
        telegramClient = telegramClientProvider.getTelegramClient();
    }

    public void Notify(long chatId, String text){
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();

        Notify(message);
    }

    public void NotifyWithInlineButtons(long chatId, String text, List<Queue<ChatInlineButton>> chatButtonRows){
        var markupBuilder = InlineKeyboardMarkup.builder();

        for (var chatButtonRow : chatButtonRows){
            var row = new InlineKeyboardRow();

            for (var button : chatButtonRow){
                var btn = InlineKeyboardButton.builder()
                    .text(button.text())
                    .callbackData(button.callbackData())
                    .build();

                row.add(btn);
            }

            markupBuilder = markupBuilder.keyboardRow(row);
        }

        InlineKeyboardMarkup markup = markupBuilder.build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();

        Notify(message);
    }

    public void NotifyWithKeyboardButtons(long chatId, String text, List<Queue<ChatKeyboardButton>> chatButtonRows){
        var replyMarkupBuilder = ReplyKeyboardMarkup.builder();

        for (var chatButtonRow : chatButtonRows){
            var row = new KeyboardRow();

            for (var button : chatButtonRow){
                row.add(button.text());
            }

            replyMarkupBuilder = replyMarkupBuilder.keyboardRow(row);
        }

        ReplyKeyboardMarkup markup = replyMarkupBuilder.build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();

        Notify(message);
    }

    private void Notify(SendMessage message){
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
