package io.github._127_0_0_l.ports.out;

import java.util.List;

public interface NotificationPort {
    void Notify(int chatId, List<String> notifications);
}
