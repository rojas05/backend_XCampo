package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.TokenNotificationID;
import com.rojas.dev.XCampo.service.Interface.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

    /**
     * Programa una lista de tareas con un intervalo de 15 minutos entre cada una.
     * @param items Lista de elementos a procesar.
     * @param taskFunction Función que recibe cada elemento y ejecuta una tarea.
     */

    @Override
    public void scheduleTasksSequentially(Queue<TokenNotificationID> items, java.util.function.Consumer<TokenNotificationID> taskFunction) {
        if (items == null || items.isEmpty()) {
            System.out.println("No hay elementos para programar.");
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            TokenNotificationID item = items.poll();
            long delay = TimeUnit.MINUTES.toSeconds(15) * i; // Escalonar cada tarea

            int finalI = i;
            taskScheduler.schedule(() -> {
                taskFunction.accept(item); // Ejecuta la función pasada desde la clase que lo llama
                if (finalI == items.size() - 1) {
                    System.out.println("✅ Última tarea ejecutada. Notificando en 15 minutos...");
                    taskScheduler.schedule(() -> System.out.println("⏳ Han pasado 15 minutos después del último procesamiento."),
                            Instant.now().plusSeconds(9));
                }
            }, Instant.now().plusSeconds(delay));
        }
    }
}
