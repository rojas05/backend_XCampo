package com.rojas.dev.XCampo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FireBaseConfig {

    @Value("${firebase.credentials.path}")
    private String credentialsPath;

    @Value("${firebase.storage.bucket}")
    private String storageBucket;


    @Value("${firebase.database.url}")
    private String databaseUrl;

    /**
     * inicializacion de firebase e ingreso de credenciales
     * @return instancia de firebase
     */
    @Bean
    public FirebaseApp initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = new ClassPathResource(credentialsPath).getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket(storageBucket)
                        .setDatabaseUrl(databaseUrl)
                        .build();

                FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado correctamente: " + firebaseApp.getName());
                return firebaseApp;
            } else {
                return FirebaseApp.getInstance();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Error initializing Firebase", e);
        }
    }

    /**
     * inicializacion de firebase
     * @return instancia de firebase database
     * @throws IOException
     */
    @Bean
    public Firestore firestore() throws IOException {
        InputStream serviceAccount = new ClassPathResource(credentialsPath).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        FirestoreOptions options = FirestoreOptions.newBuilder()
                .setCredentials(credentials)
                .build();
        System.out.println("Firebase inicializado correctamente.");
        return options.getService();
    }

    /**
     * inicio de firebase messagin
     * @param firebaseApp
     * @return instancia de firebase messagin
     */
    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        FirebaseMessaging messaging = FirebaseMessaging.getInstance(firebaseApp);
        System.out.println("FirebaseMessaging inicializado correctamente.");
        return messaging;
    }

    /**
     * inicio de fire storage y credenciales
     * @return instancia de fire storage
     * @throws IOException
     */
    @Bean
    public Storage firebaseStorage() throws IOException {
        InputStream serviceAccount = new ClassPathResource(credentialsPath).getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

        StorageOptions options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build();

        System.out.println("Firebase Storage inicializado correctamente.");
        return options.getService();
    }

    @Bean
    public FirebaseDatabase firebaseDatabase(FirebaseApp firebaseApp) {
        System.out.println("Firebase Realtime Database inicializado correctamente.");
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}