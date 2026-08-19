package com.medilinkpro.backend.controller;

import com.medilinkpro.backend.dto.request.RendezVousRequest;
import com.medilinkpro.backend.dto.request.StatutRendezVousRequest;
import com.medilinkpro.backend.dto.response.RendezVousResponse;
import com.medilinkpro.backend.service.RendezVousService;
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
@RequestMapping("/api/rendez-vous")
@RequiredArgsConstructor
@Tag(name = "Rendez-vous", description = "Prise de rendez-vous en ligne (Module 2 - F13 a F18)")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister tous les rendez-vous (reserve au personnel administratif)")
    public ResponseEntity<List<RendezVousResponse>> findAll() {
        return ResponseEntity.ok(rendezVousService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@resourceAuth.isOwnerOfRendezVous(#id, authentication.principal.id) or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Recuperer un rendez-vous par son id (patient/medecin concerne ou personnel autorise)")
    public ResponseEntity<RendezVousResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(rendezVousService.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("#patientId == authentication.principal.id or hasAnyRole('MEDECIN', 'SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister les rendez-vous d'un patient")
    public ResponseEntity<List<RendezVousResponse>> findByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(rendezVousService.findByPatient(patientId));
    }

    @GetMapping("/medecin/{medecinId}")
    @PreAuthorize("#medecinId == authentication.principal.id or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Lister les rendez-vous d'un medecin (agenda)")
    public ResponseEntity<List<RendezVousResponse>> findByMedecin(@PathVariable UUID medecinId) {
        return ResponseEntity.ok(rendezVousService.findByMedecin(medecinId));
    }

    @PostMapping
    @PreAuthorize("#request.patientId == authentication.principal.id or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(
            summary = "Prendre un rendez-vous (F13)",
            description = "Verifie la disponibilite du creneau puis confirme le rendez-vous"
    )
    public ResponseEntity<RendezVousResponse> create(@Valid @RequestBody RendezVousRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rendezVousService.create(request));
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("@resourceAuth.isOwnerOfRendezVous(#id, authentication.principal.id) or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Changer le statut d'un rendez-vous (annulation, confirmation, etc.)")
    public ResponseEntity<RendezVousResponse> updateStatut(@PathVariable UUID id, @Valid @RequestBody StatutRendezVousRequest request) {
        return ResponseEntity.ok(rendezVousService.updateStatut(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@resourceAuth.isOwnerOfRendezVous(#id, authentication.principal.id) or hasAnyRole('SECRETAIRE', 'ADMIN')")
    @Operation(summary = "Supprimer un rendez-vous")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        rendezVousService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
