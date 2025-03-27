package com.rojas.dev.XCampo.controller;

import com.rojas.dev.XCampo.entityLocation.Departamento;
import com.rojas.dev.XCampo.entityLocation.Municipio;
import com.rojas.dev.XCampo.entityLocation.Vereda;
import com.rojas.dev.XCampo.service.ServiceImp.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/firebase")
public class FirebaseController {

    @Autowired
    private FirebaseService firebaseService;

    // 📌 Guardar un departamento
    @PostMapping("/departamentos")
    public ResponseEntity<String> guardarDepartamento(@RequestBody Departamento departamento) throws ExecutionException, InterruptedException {
        String id = firebaseService.guardarPais(departamento);
        return ResponseEntity.ok("Departamento guardado con ID: " + id);
    }

    // 📌 Guardar un municipio
    @PostMapping("/municipios")
    public ResponseEntity<String> guardarMunicipio(@RequestBody Municipio municipio) throws ExecutionException, InterruptedException {
        String id = firebaseService.guardarMunicipio(municipio);
        return ResponseEntity.ok("Municipio guardado con ID: " + id);
    }

    // 📌 Guardar una vereda
    @PostMapping("/veredas")
    public ResponseEntity<String> guardarVereda(@RequestBody Vereda vereda) throws ExecutionException, InterruptedException {
        String id = firebaseService.guardarVereda(vereda);
        return ResponseEntity.ok("Vereda guardada con ID: " + id);
    }

    // 📌 Listar municipios por departamento
    @GetMapping("/municipios/{departamentoId}")
    public ResponseEntity<List<Municipio>> listarMunicipios(@PathVariable String departamentoId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(firebaseService.listarMunicipiosPorPais(departamentoId));
    }

    // 📌 Listar veredas por municipio
    @GetMapping("/veredas/{municipioId}")
    public ResponseEntity<List<Vereda>> listarVeredas(@PathVariable String municipioId) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(firebaseService.listarVeredasPorMunicipio(municipioId));
    }

    @PostMapping("veredas/agregar/{municipioId}")
    public String agregarVeredas(@PathVariable String municipioId, @RequestBody List<String> veredas) {
        return firebaseService.agregarVeredasAMunicipio(municipioId, veredas);
    }

    // 📌 Endpoint para obtener el ID de un departamento por su nombre
    @GetMapping("/departamentos/id")
    public ResponseEntity<?> obtenerIdPais(@RequestParam String nombre) {
        String paisId = firebaseService.obtenerIdDepartamentoPorNombre(nombre);
        if (paisId != null) {
            return ResponseEntity.ok(Collections.singletonMap("departamentoId", paisId));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Departamento no encontrado"));
        }
    }

    // 📌 Nuevo Endpoint para obtener todos los departamentos
    @GetMapping("/departamentos")
    public ResponseEntity<List<Departamento>> allDepartamentos() {
        try {
            List<Departamento> departamentos = firebaseService.allDepartament();
            return ResponseEntity.ok(departamentos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    // 📌 Endpoint para obtener veredas de un municipio usando solo su nombre
    @GetMapping("/veredas/municipio")
    public ResponseEntity<?> obtenerVeredasPorNombreMunicipio(@RequestParam String nombreMunicipio) {
        try {
            List<Vereda> veredas = firebaseService.obtenerVeredasPorNombreMunicipio(nombreMunicipio);
            if (veredas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("error", "No se encontraron veredas para el municipio especificado"));
            }
            return ResponseEntity.ok(veredas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error al obtener las veredas: " + e.getMessage()));
        }
    }

    // 🔹 Endpoint para obtener los municipios de un departamento dado su nombre
    @GetMapping("/municipios/get/{departamento}")
    public ResponseEntity<List<Municipio>> obtenerMunicipiosPorPais(@PathVariable String departamento) {
        try {
            List<Municipio> municipios = firebaseService.listarMunicipiosPorNombreDeDepartamento(departamento);
            return ResponseEntity.ok(municipios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }
}

