package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ChatState;
import io.github._127_0_0_l.core.models.Filters;
import io.github._127_0_0_l.core.models.TgChat;

import java.util.List;
import java.util.Optional;

public interface TgChatPort {
    Optional<Long> create(TgChat chat);

    boolean update(TgChat chat);

    boolean delete(Long id);

    Optional<TgChat> get(Long id);

    List<TgChat> get(List<Long> ids);

    List<TgChat> getAll();

    ChatState getState(Long chatId);

    List<TgChat> getByState(ChatState state);

    boolean toggleChatActivation(Long id);

    boolean updateFilters(Long chatId, Filters filters);

    boolean exists(Long id);
}
