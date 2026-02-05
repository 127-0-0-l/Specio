package io.github._127_0_0_l.infra_db.adapters;

import io.github._127_0_0_l.core.models.ChatState;
import io.github._127_0_0_l.core.models.Filters;
import io.github._127_0_0_l.core.models.TgChat;
import io.github._127_0_0_l.core.ports.out.db.TgChatPort;

import java.util.List;

public class TgChatAdapter implements TgChatPort {
    @Override
    public boolean create(TgChat chat) {
        return false;
    }

    @Override
    public boolean update(TgChat chat) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public TgChat get(int id) {
        return null;
    }

    @Override
    public List<TgChat> get(List<Integer> ids) {
        return List.of();
    }

    @Override
    public List<TgChat> getAll() {
        return List.of();
    }

    @Override
    public ChatState getState(int chatId) {
        return null;
    }

    @Override
    public List<TgChat> getActiveChats() {
        return List.of();
    }

    @Override
    public boolean toggleChatActivation(int id) {
        return false;
    }

    @Override
    public boolean updateFilters(int chatId, Filters filters) {
        return false;
    }

    @Override
    public boolean exists(int id) {
        return false;
    }

    @Override
    public String getInitMessage() {
        return "";
    }
}
