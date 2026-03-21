package io.github._127_0_0_l.infra_tg_bot.adapters.out;

import io.github._127_0_0_l.core.models.Notification;
import io.github._127_0_0_l.core.models.VehicleAdvert;
import io.github._127_0_0_l.core.ports.out.NotificationPort;
import io.github._127_0_0_l.infra_tg_bot.interfaces.NotificationService;
import io.github._127_0_0_l.infra_tg_bot.interfaces.TgBotMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationAdapter implements NotificationPort {
    private final NotificationService notificationService;
    private final TgBotMapper mapper;

    public NotificationAdapter(NotificationService notificationService,
       TgBotMapper mapper){
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    @Override
    public void notify(Notification notification) {
        notificationService.notify(notification.chatId(), notification.message());
    }

    @Override
    public void notify(Long chatId, VehicleAdvert model) {
        notificationService.notify(chatId, mapper.toBotVehicleAdvert(model));
    }
}
