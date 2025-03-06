package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.service.Interface.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private final TaskScheduler taskScheduler;

    public TaskServiceImp() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        this.taskScheduler = scheduler;
    }

    @Override
    public ScheduledFuture<?> scheduleTask(LocalDateTime targetTime, Runnable task) {
        // Calculamos la fecha de ejecución (una hora después de la hora dada)
        Date executionTime = Date.from(targetTime.plusHours(1).atZone(ZoneId.systemDefault()).toInstant());

        System.out.println("Tarea programada para ejecutarse en: " + executionTime);

        // Programamos la tarea
        return taskScheduler.schedule(task, executionTime);
    }
}
