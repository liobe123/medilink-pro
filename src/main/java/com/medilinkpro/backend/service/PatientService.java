package com.medilinkpro.backend.service;

import com.medilinkpro.backend.dto.mapper.PatientMapper;
import com.medilinkpro.backend.dto.request.PatientUpdateRequest;
import com.medilinkpro.backend.dto.response.PatientResponse;
import com.medilinkpro.backend.entity.Patient;
import com.medilinkpro.backend.entity.Utilisateur;
import com.medilinkpro.backend.enums.Role;
import com.medilinkpro.backend.exception.ResourceNotFoundException;
import com.medilinkpro.backend.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private static final int VISIBLE_SSN_DIGITS = 4;

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional(readOnly = true)
    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(patient -> maskSsnIfNeeded(patientMapper.toResponse(patient), patient.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(UUID id) {
        return maskSsnIfNeeded(patientMapper.toResponse(getPatientOrThrow(id)), id);
    }

    @Transactional
    public PatientResponse update(UUID id, PatientUpdateRequest request) {
        Patient patient = getPatientOrThrow(id);

        if (request.getNom() != null) patient.setNom(request.getNom());
        if (request.getPrenom() != null) patient.setPrenom(request.getPrenom());
        if (request.getTelephone() != null) patient.setTelephone(request.getTelephone());
        if (request.getDateNaissance() != null) patient.setDateNaissance(request.getDateNaissance());
        if (request.getGroupeSanguin() != null) patient.setGroupeSanguin(request.getGroupeSanguin());
        if (request.getAllergies() != null) patient.setAllergies(request.getAllergies());
        if (request.getAntecedents() != null) patient.setAntecedents(request.getAntecedents());
        if (request.getNumSecuriteSociale() != null) patient.setNumSecuriteSociale(request.getNumSecuriteSociale());

        return maskSsnIfNeeded(patientMapper.toResponse(patientRepository.save(patient)), id);
    }

    @Transactional
    public void delete(UUID id) {
        Patient patient = getPatientOrThrow(id);
        patientRepository.delete(patient);
    }

    private Patient getPatientOrThrow(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient non trouve avec l'id : " + id));
    }

    /**
     * Le numero de securite sociale n'est renvoye en clair qu'au patient concerne
     * (il le connait deja) ou a un role MEDECIN/ADMIN qui en a un besoin metier reel.
     * Pour les autres cas autorises a voir la fiche (ex. SECRETAIRE, qui gere les
     * rendez-vous mais n'a pas besoin du NSS complet), seuls les derniers chiffres
     * restent visibles.
     */
    private PatientResponse maskSsnIfNeeded(PatientResponse response, UUID patientId) {
        String nss = response.getNumSecuriteSociale();
        if (nss == null || nss.isBlank() || canViewFullSsn(patientId)) {
            return response;
        }
        response.setNumSecuriteSociale(maskSsn(nss));
        return response;
    }

    private boolean canViewFullSsn(UUID patientId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Utilisateur currentUser)) {
            return false;
        }
        if (currentUser.getId().equals(patientId)) {
            return true;
        }
        return currentUser.getRole() == Role.MEDECIN || currentUser.getRole() == Role.ADMIN;
    }

    private String maskSsn(String nss) {
        String compact = nss.replaceAll("\\s+", "");
        if (compact.length() <= VISIBLE_SSN_DIGITS) {
            return "*".repeat(compact.length());
        }
        String visible = compact.substring(compact.length() - VISIBLE_SSN_DIGITS);
        return "*".repeat(compact.length() - VISIBLE_SSN_DIGITS) + visible;
    }
}
