package com.medilinkpro.backend.security;

import com.medilinkpro.backend.repository.ConsultationRepository;
import com.medilinkpro.backend.repository.DossierMedicalRepository;
import com.medilinkpro.backend.repository.OrdonnanceRepository;
import com.medilinkpro.backend.repository.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Verifie qu'un utilisateur authentifie est bien le patient ou le medecin concerne
 * par une ressource medicale (dossier, consultation, ordonnance, rendez-vous) avant
 * d'autoriser l'acces. Utilise depuis les expressions @PreAuthorize des controllers.
 *
 * Si la ressource n'existe pas, on laisse passer (retourne true) : c'est le service
 * metier qui levera un ResourceNotFoundException (404), pour ne pas fuiter d'info
 * sur l'existence d'une ressource via un 403 vs 404.
 */
@Service("resourceAuth")
@RequiredArgsConstructor
public class ResourceAuthorizationService {

    private final DossierMedicalRepository dossierMedicalRepository;
    private final ConsultationRepository consultationRepository;
    private final OrdonnanceRepository ordonnanceRepository;
    private final RendezVousRepository rendezVousRepository;

    public boolean isOwnerOfDossier(UUID dossierId, UUID currentUserId) {
        return dossierMedicalRepository.findById(dossierId)
                .map(d -> d.getPatient().getId().equals(currentUserId))
                .orElse(true);
    }

    public boolean isOwnerOfConsultation(UUID consultationId, UUID currentUserId) {
        return consultationRepository.findById(consultationId)
                .map(c -> c.getPatient().getId().equals(currentUserId)
                        || c.getMedecin().getId().equals(currentUserId))
                .orElse(true);
    }

    public boolean isOwnerOfOrdonnance(UUID ordonnanceId, UUID currentUserId) {
        return ordonnanceRepository.findById(ordonnanceId)
                .map(o -> o.getPatient().getId().equals(currentUserId)
                        || o.getMedecin().getId().equals(currentUserId))
                .orElse(true);
    }

    public boolean isOwnerOfRendezVous(UUID rendezVousId, UUID currentUserId) {
        return rendezVousRepository.findById(rendezVousId)
                .map(r -> r.getPatient().getId().equals(currentUserId)
                        || r.getMedecin().getId().equals(currentUserId))
                .orElse(true);
    }
}
