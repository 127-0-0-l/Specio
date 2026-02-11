package io.github._127_0_0_l.infra_tg_bot.adapters.out;

import io.github._127_0_0_l.core.models.Notification;
import io.github._127_0_0_l.core.ports.out.NotificationPort;
import io.github._127_0_0_l.infra_tg_bot.interfaces.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class NotificationAdapter implements NotificationPort {
    private final NotificationService notificationService;

    public NotificationAdapter(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    @Override
    public void Notify(Notification notification) {
        notificationService.notify(notification.chatId(), notification.vehicleAdvert().toString());
    }
}
