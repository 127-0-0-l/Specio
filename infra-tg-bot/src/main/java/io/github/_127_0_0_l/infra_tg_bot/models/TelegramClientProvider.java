package io.github._127_0_0_l.infra_tg_bot.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public final class TelegramClientProvider {
    private final OkHttpTelegramClient telegramClient;

    public TelegramClientProvider(@Value("${bot.token}") String token){
        telegramClient = new OkHttpTelegramClient(token);
    }

    public TelegramClient getTelegramClient(){
        return telegramClient;
    }
}
