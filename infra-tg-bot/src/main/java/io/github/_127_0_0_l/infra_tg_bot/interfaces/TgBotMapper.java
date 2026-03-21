package io.github._127_0_0_l.infra_tg_bot.interfaces;

import java.util.List;

import org.mapstruct.Mapper;

import io.github._127_0_0_l.core.models.*;
import io.github._127_0_0_l.infra_tg_bot.models.Chat;

@Mapper(componentModel = "spring")
public interface TgBotMapper {
    TgChat toCoreTgChat (Chat model);
    Chat toBotChat (TgChat model);

    io.github._127_0_0_l.infra_tg_bot.models.ChatState toBotChatState (ChatState model);

    Filters toBotFilters (io.github._127_0_0_l.infra_tg_bot.models.Filters model);

    List<Region> toCoreRegions (List<io.github._127_0_0_l.infra_tg_bot.models.Region> model);
    List<io.github._127_0_0_l.infra_tg_bot.models.Region> toBotRegions (List<Region> model);

    List<City> toCoreCities (List<io.github._127_0_0_l.infra_tg_bot.models.City> model);
    List<io.github._127_0_0_l.infra_tg_bot.models.City> toBotCities (List<City> model);

    io.github._127_0_0_l.infra_tg_bot.models.VehicleAdvert toBotVehicleAdvert (VehicleAdvert model);
}
