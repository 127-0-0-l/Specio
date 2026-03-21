package io.github._127_0_0_l.core.ports.out;

import io.github._127_0_0_l.core.models.Notification;
import io.github._127_0_0_l.core.models.VehicleAdvert;

public interface NotificationPort {
    void notify(Notification notification);

    void notify(Long chatId, VehicleAdvert model);
}
