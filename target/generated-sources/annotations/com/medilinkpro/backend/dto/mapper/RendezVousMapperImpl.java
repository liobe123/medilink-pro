package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.RendezVousResponse;
import com.medilinkpro.backend.entity.EtablissementSante;
import com.medilinkpro.backend.entity.Medecin;
import com.medilinkpro.backend.entity.Patient;
import com.medilinkpro.backend.entity.RendezVous;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:15:47+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class RendezVousMapperImpl implements RendezVousMapper {

    @Override
    public RendezVousResponse toResponse(RendezVous rdv) {
        if ( rdv == null ) {
            return null;
        }

        RendezVousResponse.RendezVousResponseBuilder rendezVousResponse = RendezVousResponse.builder();

        rendezVousResponse.patientId( rdvPatientId( rdv ) );
        rendezVousResponse.medecinId( rdvMedecinId( rdv ) );
        rendezVousResponse.specialiteMedecin( rdvMedecinSpecialite( rdv ) );
        rendezVousResponse.etablissementId( rdvEtablissementId( rdv ) );
        rendezVousResponse.etablissementNom( rdvEtablissementNom( rdv ) );
        rendezVousResponse.id( rdv.getId() );
        rendezVousResponse.dateHeure( rdv.getDateHeure() );
        rendezVousResponse.statut( rdv.getStatut() );
        rendezVousResponse.type( rdv.getType() );
        rendezVousResponse.rappelEnvoye( rdv.isRappelEnvoye() );
        rendezVousResponse.codeConfirmation( rdv.getCodeConfirmation() );
        rendezVousResponse.creeLe( rdv.getCreeLe() );

        rendezVousResponse.patientNomComplet( rdv.getPatient().getPrenom() + " " + rdv.getPatient().getNom() );
        rendezVousResponse.medecinNomComplet( rdv.getMedecin().getPrenom() + " " + rdv.getMedecin().getNom() );

        return rendezVousResponse.build();
    }

    private UUID rdvPatientId(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Patient patient = rendezVous.getPatient();
        if ( patient == null ) {
            return null;
        }
        UUID id = patient.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID rdvMedecinId(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Medecin medecin = rendezVous.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        UUID id = medecin.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String rdvMedecinSpecialite(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        Medecin medecin = rendezVous.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        String specialite = medecin.getSpecialite();
        if ( specialite == null ) {
            return null;
        }
        return specialite;
    }

    private UUID rdvEtablissementId(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        EtablissementSante etablissement = rendezVous.getEtablissement();
        if ( etablissement == null ) {
            return null;
        }
        UUID id = etablissement.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String rdvEtablissementNom(RendezVous rendezVous) {
        if ( rendezVous == null ) {
            return null;
        }
        EtablissementSante etablissement = rendezVous.getEtablissement();
        if ( etablissement == null ) {
            return null;
        }
        String nom = etablissement.getNom();
        if ( nom == null ) {
            return null;
        }
        return nom;
    }
}
