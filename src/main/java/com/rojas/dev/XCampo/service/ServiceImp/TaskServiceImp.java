package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.dto.TokenNotificationID;
import com.rojas.dev.XCampo.enumClass.DeliveryProductState;
import com.rojas.dev.XCampo.repository.DeliveryRepository;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class TaskServiceImp implements TaskService {

    @Autowired
    private final TaskScheduler taskScheduler;

    @Autowired
    DeliveryRepository deliveryRepository;

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
     *
     * @param items        Lista de elementos a procesar.
     * @param taskFunction Función que recibe cada elemento y ejecuta una tarea.
     */

    @Override
    public void scheduleTasksSequentially(Queue<TokenNotificationID> items,  Runnable task, java.util.function.Consumer<TokenNotificationID> taskFunction) {
        if (items == null || items.isEmpty()) {
            System.out.println("No hay elementos para programar.");
            return;
        }

        int totalItems = items.size();
        AtomicInteger counter = new AtomicInteger(0);

        while (!items.isEmpty()) {
            TokenNotificationID item = items.poll();
            if (item == null) continue;
            long delay = TimeUnit.MINUTES.toSeconds(15) * counter.getAndIncrement();

            taskScheduler.schedule(() -> {
                try {
                    List<Long> pendingDeliveries = item.getDelivery().stream()
                            .filter(deliveryId -> !deliveryRepository.verificateStateById(deliveryId, DeliveryProductState.EN_COLA))
                            .collect(Collectors.toList());

                    if (pendingDeliveries.isEmpty()) {
                        System.out.println("✅ Todos los envios ya han sido tomados. Deteniendo las notification...");
                        return;
                    }

                    TokenNotificationID updatedItem = new TokenNotificationID(pendingDeliveries, item.getToken());
                    taskFunction.accept(updatedItem);

                    if (counter.get() == totalItems) {
                        taskScheduler.schedule(() -> {
                            System.out.println("⏳ Nadie aceptó el pedido en 15 minutos.");
                            LocalDateTime now = LocalDateTime.now();
                            this.scheduleTask(now,task);
                            }, Instant.now().plusSeconds(900)); // 15 minutos
                    }
                } catch (Exception e) {
                    System.err.println("❌ Error en la tarea para los envíos " + item.getDelivery() + ": " + e.getMessage());
                }
            }, Instant.now().plusSeconds(delay));
        }
    }
}
