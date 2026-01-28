package io.github._127_0_0_l.core.models;

public record TgChat(
        int chatId,
        boolean isActive,
        ChatState state,
        Filters filters
) { }
