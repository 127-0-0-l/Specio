package io.github._127_0_0_l.infra_tg_bot.interfaces;

import org.mapstruct.Mapper;

import io.github._127_0_0_l.core.models.*;
import io.github._127_0_0_l.infra_tg_bot.models.Chat;

@Mapper(componentModel = "spring")
public interface TgBotMapper {
    TgChat toCoreTgChat (Chat model);
    Chat toBotChat (TgChat model);

    io.github._127_0_0_l.infra_tg_bot.models.ChatState toBotChatState (ChatState model);

    Filters toBotFilters (io.github._127_0_0_l.infra_tg_bot.models.Filters model);
}
