package io.github._127_0_0_l.infra_tg_bot.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Service
public class TgBotService implements SpringLongPollingBot {
    private final String botToken;
    private final UpdateHandler updateHandler;

    public TgBotService(@Value("${bot.token}") String token,
                        UpdateHandler updateHandler) {
        this.botToken = token;
        this.updateHandler = updateHandler;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateHandler;
    }
}
