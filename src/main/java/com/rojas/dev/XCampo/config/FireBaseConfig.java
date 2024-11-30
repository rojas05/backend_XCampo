package com.rojas.dev.XCampo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseConfig {

    @Bean
    public FirebaseApp initializeFirebase(
            @Value("${firebase.credentials.path}") String credentialsPath,
            @Value("${firebase.storage.bucket}") String storageBucket
    ) {
        try {
            InputStream serviceAccount = getClass()
                    .getClassLoader()
                    .getResourceAsStream(credentialsPath);
            //new FileInputStream("src/main/resources/agromarket-3d34d-firebase-adminsdk-flhqr-6a352e2853.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(storageBucket) //  "agromarket-3d34d.appspot.com"
                    .build();

            return FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            throw new IllegalStateException("Error initializing Firebase", e);
        }
    }
}
