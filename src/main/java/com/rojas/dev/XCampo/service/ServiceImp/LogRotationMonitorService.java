package com.rojas.dev.XCampo.service.ServiceImp;

import com.rojas.dev.XCampo.service.Interface.FirebaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Service
public class LogRotationMonitorService {

    private static final String LOGS_FOLDER = "logs";
    private final Set<String> procesados = new HashSet<>();

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    @Scheduled(cron = "0 0 0 * * ?") // Revisa cada 60 segundos
    public void scanAndPushLogs() {
        File folder = new File(LOGS_FOLDER);
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }

        for (File archivo : folder.listFiles()) {
            if (archivo.isFile() && !procesados.contains(archivo.getName())) {
                // Subir archivo a Firebase Storage
                String enlace = firebaseStorageService.uploadLogFile(archivo.getAbsolutePath(), archivo.getName());
                if (enlace != null) {
                    procesados.add(archivo.getName());
                    System.out.println("Archivo subido: " + archivo.getName());
                }
            }
        }
    }
}
