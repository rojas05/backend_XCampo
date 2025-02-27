package com.rojas.dev.XCampo.service.ServiceImp;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.rojas.dev.XCampo.entityLocation.Departamento;
import com.rojas.dev.XCampo.entityLocation.Municipio;
import com.rojas.dev.XCampo.entityLocation.Vereda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    private final Firestore firestore;

    @Autowired
    public FirebaseService(Firestore firestore) {
        this.firestore = firestore;
    }

    // 📌 Guardar un país en Firebase
    public String guardarPais(Departamento departamento) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("departamentos").document();
        departamento.setId(docRef.getId());
        docRef.set(departamento).get();
        return departamento.getId();
    }

    // 📌 Guardar un municipio en Firebase
    public String guardarMunicipio(Municipio municipio) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("municipios").document();
        municipio.setId(docRef.getId());
        docRef.set(municipio).get();
        return municipio.getId();
    }

    // 📌 Guardar una vereda en Firebase
    public String guardarVereda(Vereda vereda) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("veredas").document();
        vereda.setId(docRef.getId());
        docRef.set(vereda).get();
        return vereda.getId();
    }

    // 📌 Listar municipios de un país
    public List<Municipio> listarMunicipiosPorPais(String paisId) throws ExecutionException, InterruptedException {
        List<Municipio> municipios = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection("municipios")
                .whereEqualTo("departamentoId", paisId)
                .get();
        for (DocumentSnapshot doc : future.get().getDocuments()) {
            municipios.add(doc.toObject(Municipio.class));
        }
        return municipios;
    }

    // 📌 Listar veredas de un municipio
    public List<Vereda> listarVeredasPorMunicipio(String municipioId) throws ExecutionException, InterruptedException {
        List<Vereda> veredas = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection("veredas")
                .whereEqualTo("municipioId", municipioId)
                .get();
        for (DocumentSnapshot doc : future.get().getDocuments()) {
            veredas.add(doc.toObject(Vereda.class));
        }
        return veredas;
    }

    public String agregarVeredasAMunicipio(String municipioId, List<String> veredas) {
        try {
            // 🔹 Verificar si el municipio existe antes de agregar veredas
            DocumentReference municipioRef = firestore.collection("municipios").document(municipioId);
            if (!municipioRef.get().get().exists()) {
                return "Error: El municipio con ID " + municipioId + " no existe en Firestore.";
            }

            // 🔹 Referencia a la subcolección "veredas" dentro del municipio
            CollectionReference veredasRef = firestore.collection("veredas");
            WriteBatch batch = firestore.batch();

            for (String nombreVereda : veredas) {
                // 🔹 Crear un nuevo documento para cada vereda
                DocumentReference veredaDoc = veredasRef.document();

                // 🔹 Crear el objeto de vereda con nombre e ID del municipio
                Map<String, Object> veredaData = new HashMap<>();
                veredaData.put("id", veredaDoc.getId());
                veredaData.put("nombre", nombreVereda);
                veredaData.put("municipioId", municipioId);

                batch.set(veredaDoc, veredaData);
            }

            // 🔹 Ejecutar la escritura en lote
            batch.commit().get();
            return "✅ Veredas agregadas correctamente al municipio con ID " + municipioId;

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error al agregar veredas: " + e.getMessage();
        }


    }

    public String obtenerIdDepartamentoPorNombre(String nombre) {
        try {
            // 🔍 Buscar en la colección "paises" el país con el nombre dado
            ApiFuture<QuerySnapshot> future = firestore.collection("departamentos")
                    .whereEqualTo("nombre", nombre)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            if (!documents.isEmpty()) {
                return documents.get(0).getId(); // Retorna el ID del primer resultado encontrado
            } else {
                return null; // Si no encuentra el país, retorna null
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Departamento> allDepartament() throws ExecutionException, InterruptedException {
        List<Departamento> departamentos = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = firestore.collection("departamentos").get();

        for (DocumentSnapshot doc : future.get().getDocuments()) {
            Departamento departamento = doc.toObject(Departamento.class);
            if (departamento != null) {
                departamento.setId(doc.getId()); // Asignar el ID del documento al objeto
                departamentos.add(departamento);
            }
        }
        return departamentos;
    }

    public List<Vereda> obtenerVeredasPorNombreMunicipio(String nombreMunicipio) throws ExecutionException, InterruptedException {
        List<Vereda> veredas = new ArrayList<>();

        // 📌 Buscar el ID del municipio basado en su nombre
        ApiFuture<QuerySnapshot> municipioFuture = firestore.collection("municipios")
                .whereEqualTo("nombre", nombreMunicipio)
                .get();

        List<QueryDocumentSnapshot> municipios = municipioFuture.get().getDocuments();

        if (municipios.isEmpty()) {
            return veredas; // No se encontró el municipio
        }

        String municipioId = municipios.get(0).getId(); // Tomamos el primer resultado

        // 📌 Buscar las veredas asociadas a ese municipio
        ApiFuture<QuerySnapshot> veredasFuture = firestore.collection("veredas")
                .whereEqualTo("municipioId", municipioId)
                .get();

        for (DocumentSnapshot doc : veredasFuture.get().getDocuments()) {
            Vereda vereda = doc.toObject(Vereda.class);
            if (vereda != null) {
                vereda.setId(doc.getId()); // Asignar ID del documento
                veredas.add(vereda);
            }
        }
        return veredas;
    }

    public List<Municipio> listarMunicipiosPorNombreDeDepartamento(String paisNombre) throws ExecutionException, InterruptedException {
        // 1️⃣ Buscar el ID del país usando su nombre
        String paisId = obtenerIdDepartamentoPorNombre(paisNombre);
        if (paisId == null) {
            return Collections.emptyList();
        }

        // 2️⃣ Listar los municipios filtrando por el ID del país
        return listarMunicipiosPorPais(paisId);
    }

}
