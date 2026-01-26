package io.github._127_0_0_l.models;

public record TgChat(
        int chatId,
        boolean isActive,
        ChatState state,
        Filters filters
) { }
