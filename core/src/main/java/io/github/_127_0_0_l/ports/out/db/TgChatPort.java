package io.github._127_0_0_l.ports.out.db;

import io.github._127_0_0_l.models.TgChat;

import java.util.List;

public interface TgChatPort {
    boolean create(TgChat tgChat);

    boolean update(TgChat tgChat);

    boolean delete(int tgChatId);

    TgChat get(int tgChatId);

    List<TgChat> getAll();
}
