package io.github._127_0_0_l.ports.in;

import io.github._127_0_0_l.models.TgChat;

public interface TgBotUseCase {
    boolean createChat(TgChat tgChat);

    boolean updateChat(TgChat tgChat);

    boolean deleteChat(TgChat tgChat);
}
