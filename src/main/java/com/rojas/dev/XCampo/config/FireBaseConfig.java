package com.rojas.dev.XCampo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

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
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource("agromarket-3d34d-firebase-adminsdk-flhqr-9ca1979333.json").getInputStream()))
                        .build();
                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado correctamente: " + firebaseApp.getName());
                return firebaseApp;
            } else {
                return FirebaseApp.getInstance(); // Devuelve la instancia existente
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error initializing Firebase", e);
        }
    }

    @Bean
    public Firestore firestore() throws IOException {
        InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("agromarket-3d34d-firebase-adminsdk-flhqr-9ca1979333.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        return options.getService();
    }


    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        FirebaseMessaging messaging = FirebaseMessaging.getInstance(firebaseApp);
        System.out.println("FirebaseMessaging inicializado correctamente.");
        return messaging;
    }


}
