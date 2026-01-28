package io.github._127_0_0_l.core.ports.out.db;

import io.github._127_0_0_l.core.models.ChatState;

import java.util.List;

public interface ChatStatePort {
    boolean createChatState(ChatState chatState);

    boolean deleteChatState(String chatState);

    List<String> getChatStates();
}
