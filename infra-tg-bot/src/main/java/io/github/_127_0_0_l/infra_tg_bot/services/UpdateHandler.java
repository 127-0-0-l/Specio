package io.github._127_0_0_l.infra_tg_bot.services;

import io.github._127_0_0_l.infra_tg_bot.models.TelegramClientProvider;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Service
public class UpdateHandler implements LongPollingUpdateConsumer {
    private final TelegramClient telegramClient;

    public UpdateHandler(TelegramClientProvider telegramClientProvider){
        telegramClient = telegramClientProvider.getTelegramClient();
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                long chatId = update.getMessage().getChatId();
                sendTextInline(chatId, update.getMessage().getText());
            }

            if (update.hasCallbackQuery()){
                long chatId = update.getCallbackQuery().getMessage().getChatId();
                String callback = update.getCallbackQuery().getData();
                sendText(chatId, callback);
            }
        }
    }

    private void sendText(long chatId, String text) {
        KeyboardRow krow = new KeyboardRow();
        krow.add("key 1");
        krow.add("key 2");
        ReplyKeyboardMarkup replyMarkup = ReplyKeyboardMarkup.builder()
                .keyboardRow(krow)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyMarkup)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendTextInline(long chatId, String text) {
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text("click")
                .callbackData("/click")
                .build();

        var b2 = InlineKeyboardButton.builder()
                .text("push")
                .callbackData("/push")
                .build();

        var b3 = InlineKeyboardButton.builder()
                .text("2")
                .callbackData("/2")
                .build();

        var b4 = InlineKeyboardButton.builder()
                .text("3")
                .callbackData("/3")
                .build();

        InlineKeyboardRow row = new InlineKeyboardRow(button);
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(b2);
        row2.add(b3);
        row2.add(b4);

        InlineKeyboardMarkup markup = InlineKeyboardMarkup.builder()
                .keyboardRow(row)
                .keyboardRow(row2)
                .build();

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
