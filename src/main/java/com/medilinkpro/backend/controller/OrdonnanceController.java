package com.medilinkpro.backend.controller;

import com.medilinkpro.backend.dto.request.OrdonnanceRequest;
import com.medilinkpro.backend.dto.response.OrdonnanceResponse;
import com.medilinkpro.backend.service.OrdonnanceService;
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
@RequestMapping("/api/ordonnances")
@RequiredArgsConstructor
@Tag(name = "Ordonnances", description = "Ordonnances numeriques avec code QR verifiable (Module 1 - F05)")
public class OrdonnanceController {

    private final OrdonnanceService ordonnanceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister toutes les ordonnances (reserve au personnel medical/administratif)")
    public ResponseEntity<List<OrdonnanceResponse>> findAll() {
        return ResponseEntity.ok(ordonnanceService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@resourceAuth.isOwnerOfOrdonnance(#id, authentication.principal.id) or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Recuperer une ordonnance par son id (patient/medecin concerne ou personnel autorise)")
    public ResponseEntity<OrdonnanceResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ordonnanceService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("#patientId == authentication.principal.id or hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister les ordonnances d'un patient")
    public ResponseEntity<List<OrdonnanceResponse>> findByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(ordonnanceService.findByPatient(patientId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MEDECIN', 'ADMIN')")
    @Operation(summary = "Generer une ordonnance numerique pour une consultation (reserve au medecin)")
    public ResponseEntity<OrdonnanceResponse> create(@Valid @RequestBody OrdonnanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordonnanceService.create(request));
    }
}
