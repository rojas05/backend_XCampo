package com.rojas.dev.XCampo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    /**
     * configuracion para metodos asincronos
     * @return
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Minimo de tareas
        executor.setMaxPoolSize(10); // Maximo de tareas
        executor.setQueueCapacity(50); // Capacidad de la cola en espera
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    };
}
