package io.github._127_0_0_l.models;

import java.util.List;

public record User (
        int chatId,
        boolean isActive,
        ChatState state,
        Filters filters
) { }
