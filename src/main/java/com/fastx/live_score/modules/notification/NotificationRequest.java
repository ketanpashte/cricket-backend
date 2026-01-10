package com.fastx.live_score.modules.notification;

import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private String message;
    private String targetType;
    private String targetValue;
}
