package com.medilinkpro.backend.controller;

import com.medilinkpro.backend.dto.request.RegisterRequest;
import com.medilinkpro.backend.dto.response.AuthResponse;
import com.medilinkpro.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Administration des comptes du personnel. Reserve aux administrateurs :
 * la creation de comptes MEDECIN, SECRETAIRE, DIRECTEUR ou ADMIN ne doit
 * jamais passer par l'inscription publique (/api/auth/register).
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Creation de comptes du personnel (reserve aux administrateurs)")
public class AdminController {

    private final AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creer un compte du personnel (MEDECIN, SECRETAIRE, DIRECTEUR ou ADMIN)")
    public ResponseEntity<AuthResponse> createStaffAccount(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerStaff(request));
    }
}
