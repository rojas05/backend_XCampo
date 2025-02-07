package com.rojas.dev.XCampo.service.ServiceImp;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.rojas.dev.XCampo.service.Interface.FirebaseStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class FirebaseStorageServiceImp implements FirebaseStorageService {

    @Override
    public ResponseEntity<?> uploadMultipleFile(List<MultipartFile> images, Long idSeller, String context){
        String url = "";
        for (MultipartFile image : images) {
            try {
                String imageUrl = uploadFile(image, idSeller, context);
                url = url.concat(imageUrl+ " ");
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred " + e);
            }
        }
        return ResponseEntity.ok(url);
    }

    private String uploadFile(MultipartFile file, Long idSeller, String context) throws IOException {

        final String path = context+"/"+idSeller+"/";

        String fileName = file.getOriginalFilename();

        Bucket bucket = StorageClient.getInstance().bucket();

        deleteFile(path + fileName);

        Blob blob = bucket.create(path + fileName, file.getBytes(), file.getContentType());

        String encodedPath = URLEncoder.encode(blob.getName(), StandardCharsets.UTF_8.toString());
        String fileUrl = String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                encodedPath
        );

        return fileUrl;

        //return blob.getMediaLink();
    }

    private void deleteFile(String filePath) {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(filePath);
        if (blob != null) {
            blob.delete();
        }
    }

    @Override
    public String uploadLogFile(String rutaArchivo, String nombreArchivo) {
        try {
            File archivo = new File(rutaArchivo);
            FileInputStream contenido = new FileInputStream(archivo);

            Bucket bucket = StorageClient.getInstance().bucket();
            Blob blob = bucket.create("logs/" + nombreArchivo, contenido, "text/plain");

            return blob.getMediaLink(); // Devuelve el enlace público del archivo
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
