package io.github._127_0_0_l.ports.out.db;

import io.github._127_0_0_l.models.ChatState;

import java.util.List;

public interface ChatStatePort {
    boolean createChatState(ChatState chatState);

    boolean deleteChatState(String chatState);

    List<String> getChatStates();
}
