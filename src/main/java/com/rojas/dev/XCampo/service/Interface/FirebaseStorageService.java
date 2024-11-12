package com.rojas.dev.XCampo.service.Interface;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FirebaseStorageService {
    ResponseEntity<?> uploadMultipleFile(List<MultipartFile> images, Long idSeller, String context);
}