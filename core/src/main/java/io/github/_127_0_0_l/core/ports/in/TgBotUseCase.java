package io.github._127_0_0_l.core.ports.in;

import io.github._127_0_0_l.core.models.TgChat;

public interface TgBotUseCase {
    boolean createChat(TgChat tgChat);

    boolean updateChat(TgChat tgChat);

    boolean deleteChat(TgChat tgChat);
}
