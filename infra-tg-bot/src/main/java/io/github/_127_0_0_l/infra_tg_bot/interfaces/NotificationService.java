package io.github._127_0_0_l.infra_tg_bot.interfaces;

import io.github._127_0_0_l.infra_tg_bot.models.ChatInlineButton;
import io.github._127_0_0_l.infra_tg_bot.models.ChatKeyboardButton;

import java.util.List;
import java.util.Queue;

public interface NotificationService {
    void Notify(long chatId, String text);

    void NotifyWithInlineButtons(long chatId, String text, List<Queue<ChatInlineButton>> chatButtonRows);

    void NotifyWithKeyboardButtons(long chatId, String text, List<Queue<ChatKeyboardButton>> chatButtonRows);
}
