package com.rojas.dev.XCampo.service.Interface;

import com.rojas.dev.XCampo.dto.TokenNotificationID;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

public interface TaskService {
    ScheduledFuture<?> scheduleTask(LocalDateTime targetTime, Runnable task);

    void scheduleTasksSequentially(Queue<TokenNotificationID> items, java.util.function.Consumer<TokenNotificationID> taskFunction);
}
