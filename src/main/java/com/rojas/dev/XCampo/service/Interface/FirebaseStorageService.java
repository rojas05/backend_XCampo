package com.rojas.dev.XCampo.service.Interface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FirebaseStorageService {

    ResponseEntity<?> uploadMultipleFile(List<MultipartFile> file, Long idSeller, String context);

    String uploadLogFile(String rutaArchivo, String nombreArchivo);
}