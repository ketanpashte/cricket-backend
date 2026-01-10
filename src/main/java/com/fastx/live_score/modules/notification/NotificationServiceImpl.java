package com.fastx.live_score.modules.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public NotificationEntity sendNotification(NotificationRequest request) {
        NotificationEntity entity = new NotificationEntity();
        entity.setTitle(request.getTitle());
        entity.setMessage(request.getMessage());
        entity.setTargetType(request.getTargetType());
        entity.setTargetValue(request.getTargetValue());

        // Logic to actually trigger push notification (e.g., via FCM) would go here.
        // For now, we are just storing it in the database.

        return notificationRepository.save(entity);
    }

    @Override
    public List<NotificationEntity> getNotificationHistory() {
        return notificationRepository.findAllByOrderBySentAtDesc();
    }
}
