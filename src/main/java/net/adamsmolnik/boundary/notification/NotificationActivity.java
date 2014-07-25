package net.adamsmolnik.boundary.notification;

import net.adamsmolnik.model.notification.NotificationRequest;
import com.amazonaws.services.simpleworkflow.flow.annotations.Activities;
import com.amazonaws.services.simpleworkflow.flow.annotations.ActivityRegistrationOptions;

/**
 * @author ASmolnik
 *
 */
@ActivityRegistrationOptions(defaultTaskScheduleToStartTimeoutSeconds = 300, defaultTaskStartToCloseTimeoutSeconds = 10)
@Activities(version = "1.0")
public interface NotificationActivity {

    void publish(NotificationRequest notificationRequest);

}
