package com.medilinkpro.backend.controller;

import com.medilinkpro.backend.dto.request.PatientUpdateRequest;
import com.medilinkpro.backend.dto.response.PatientResponse;
import com.medilinkpro.backend.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Gestion des patients et de leurs informations medicales de base")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister tous les patients (reserve au personnel medical/administratif)")
    public ResponseEntity<List<PatientResponse>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Recuperer un patient par son id (soi-meme ou personnel autorise)")
    public ResponseEntity<PatientResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Mettre a jour un patient (soi-meme ou personnel autorise)")
    public ResponseEntity<PatientResponse> update(@PathVariable UUID id, @Valid @RequestBody PatientUpdateRequest request) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Supprimer un patient (reserve au personnel administratif)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
