package io.github._127_0_0_l.infra_tg_bot.interfaces;

import io.github._127_0_0_l.infra_tg_bot.models.ChatInlineButton;
import io.github._127_0_0_l.infra_tg_bot.models.ChatKeyboardButton;

import java.util.List;
import java.util.Queue;

public interface NotificationService {
    void notify(long chatId, String text);

    void notifyWithInlineButtons(long chatId, String text, List<Queue<ChatInlineButton>> chatButtonRows);

    void notifyWithKeyboardButtons(long chatId, String text, List<Queue<ChatKeyboardButton>> chatButtonRows);
}
