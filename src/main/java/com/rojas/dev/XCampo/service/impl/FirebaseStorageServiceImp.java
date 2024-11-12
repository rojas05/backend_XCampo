package com.rojas.dev.XCampo.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.rojas.dev.XCampo.service.Interface.FirebaseStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
                url = url.concat("[" + imageUrl +"]"+ " ");
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error occurred " + e);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(url);
    }


    private String uploadFile(MultipartFile file, Long idSeller, String context) throws IOException {

        final String phat = context+"/"+idSeller+"/";

        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();

        Bucket bucket = StorageClient.getInstance().bucket();

        Blob blob = bucket.create(phat+fileName,file.getBytes(),file.getContentType());

        return blob.getMediaLink();
    }

}
