package com.medilinkpro.backend.service;

import com.medilinkpro.backend.dto.request.LoginRequest;
import com.medilinkpro.backend.dto.request.RegisterRequest;
import com.medilinkpro.backend.dto.response.AuthResponse;
import com.medilinkpro.backend.entity.*;
import com.medilinkpro.backend.enums.Role;
import com.medilinkpro.backend.exception.BadRequestException;
import com.medilinkpro.backend.repository.DossierMedicalRepository;
import com.medilinkpro.backend.repository.UtilisateurRepository;
import com.medilinkpro.backend.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service d'authentification : inscription multi-role et connexion (login) avec emission d'un JWT.
 * Le type concret d'Utilisateur cree depend du role transmis dans la requete d'inscription.
 * Pour un Patient, un DossierMedical (DME) vide est automatiquement cree (F01),
 * conformement a l'association "1 Patient -> 1 DossierMedical" du diagramme de classes.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un compte existe deja avec cet email");
        }

        Utilisateur utilisateur = buildUtilisateur(request);
        Utilisateur saved = utilisateurRepository.save(utilisateur);

        if (saved instanceof Patient patient) {
            DossierMedical dossier = DossierMedical.builder()
                    .patient(patient)
                    .chiffrementActif(true)
                    .build();
            dossierMedicalRepository.save(dossier);
        }

        String token = jwtService.generateToken(saved);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(saved.getId())
                .email(saved.getEmail())
                .nom(saved.getNom())
                .prenom(saved.getPrenom())
                .role(saved.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Email ou mot de passe incorrect"));

        String token = jwtService.generateToken(utilisateur);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(utilisateur.getId())
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .role(utilisateur.getRole())
                .build();
    }

    private Utilisateur buildUtilisateur(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getMotDePasse());
        Role role = request.getRole();

        return switch (role) {
            case PATIENT -> Patient.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(encodedPassword)
                    .telephone(request.getTelephone())
                    .actif(true)
                    .dateNaissance(request.getDateNaissance())
                    .groupeSanguin(request.getGroupeSanguin())
                    .allergies(request.getAllergies())
                    .antecedents(request.getAntecedents())
                    .numSecuriteSociale(request.getNumSecuriteSociale())
                    .build();

            case MEDECIN -> Medecin.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(encodedPassword)
                    .telephone(request.getTelephone())
                    .actif(true)
                    .specialite(request.getSpecialite())
                    .numeroOrdre(request.getNumeroOrdre())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .tarif(request.getTarif())
                    .verifie(false)
                    .build();

            case ADMIN -> Admin.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(encodedPassword)
                    .telephone(request.getTelephone())
                    .actif(true)
                    .build();

            case DIRECTEUR -> Directeur.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(encodedPassword)
                    .telephone(request.getTelephone())
                    .actif(true)
                    .build();

            case SECRETAIRE -> Secretaire.builder()
                    .nom(request.getNom())
                    .prenom(request.getPrenom())
                    .email(request.getEmail())
                    .motDePasse(encodedPassword)
                    .telephone(request.getTelephone())
                    .actif(true)
                    .build();
        };
    }
}
