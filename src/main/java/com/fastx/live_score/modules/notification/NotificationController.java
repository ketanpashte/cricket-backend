package com.fastx.live_score.modules.notification;

import com.fastx.live_score.core.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.fastx.live_score.core.config.APiConfig.API_VERSION_1;

@RestController
@RequestMapping(API_VERSION_1 + "/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public AppResponse<NotificationEntity> sendNotification(@RequestBody NotificationRequest request) {
        return AppResponse.success(notificationService.sendNotification(request));
    }

    @GetMapping("/history")
    public AppResponse<List<NotificationEntity>> getNotificationHistory() {
        return AppResponse.success(notificationService.getNotificationHistory());
    }
}
