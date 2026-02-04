package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ChatState;
import io.github._127_0_0_l.core.models.Filters;
import io.github._127_0_0_l.core.models.TgChat;

import java.util.List;

public interface TgChatPort {
    boolean create(TgChat chat);

    boolean update(TgChat chat);

    boolean delete(int id);

    TgChat get(int id);

    List<TgChat> get(List<Integer> ids);

    List<TgChat> getAll();

    ChatState getState(int chatId);

    List<TgChat> getActiveChats();

    boolean toggleChatActivation(int id);

    boolean updateFilters(int chatId, Filters filters);

    boolean exists(int id);

    String getInitMessage();
}
