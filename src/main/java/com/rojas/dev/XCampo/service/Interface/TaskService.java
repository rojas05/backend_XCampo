package com.rojas.dev.XCampo.service.Interface;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;

public interface TaskService {
    ScheduledFuture<?> scheduleTask(LocalDateTime targetTime, Runnable task);
}
