package com.fastx.live_score.modules.notification;

import java.util.List;

public interface NotificationService {
    NotificationEntity sendNotification(NotificationRequest request);

    List<NotificationEntity> getNotificationHistory();
}
