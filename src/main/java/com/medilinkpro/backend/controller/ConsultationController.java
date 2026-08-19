package com.medilinkpro.backend.controller;

import com.medilinkpro.backend.dto.request.ConsultationRequest;
import com.medilinkpro.backend.dto.response.ConsultationResponse;
import com.medilinkpro.backend.service.ConsultationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
@Tag(name = "Consultations", description = "Comptes rendus medicaux, diagnostics (Module 1 - F03)")
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister toutes les consultations (reserve au personnel medical/administratif)")
    public ResponseEntity<List<ConsultationResponse>> findAll() {
        return ResponseEntity.ok(consultationService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@resourceAuth.isOwnerOfConsultation(#id, authentication.principal.id) or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Recuperer une consultation par son id (patient/medecin concerne ou personnel autorise)")
    public ResponseEntity<ConsultationResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(consultationService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("#patientId == authentication.principal.id or hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister les consultations d'un patient")
    public ResponseEntity<List<ConsultationResponse>> findByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(consultationService.findByPatient(patientId));
    }

    @GetMapping("/medecin/{medecinId}")
    @PreAuthorize("#medecinId == authentication.principal.id or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister les consultations realisees par un medecin")
    public ResponseEntity<List<ConsultationResponse>> findByMedecin(@PathVariable UUID medecinId) {
        return ResponseEntity.ok(consultationService.findByMedecin(medecinId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEDECIN', 'ADMIN')")
    @Operation(summary = "Enregistrer une nouvelle consultation (reserve au medecin)")
    public ResponseEntity<ConsultationResponse> create(@Valid @RequestBody ConsultationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationService.create(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDECIN', 'ADMIN')")
    @Operation(summary = "Supprimer une consultation (reserve au medecin)")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        consultationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
