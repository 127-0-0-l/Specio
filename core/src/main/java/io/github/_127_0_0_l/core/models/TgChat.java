package io.github._127_0_0_l.core.models;

public record TgChat(
        Long id,
        ChatState state,
        Filters filters,
        int lastMessageId
) { }
