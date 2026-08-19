package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.ConsultationResponse;
import com.medilinkpro.backend.entity.Consultation;
import com.medilinkpro.backend.entity.DossierMedical;
import com.medilinkpro.backend.entity.Medecin;
import com.medilinkpro.backend.entity.Ordonnance;
import com.medilinkpro.backend.entity.Patient;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:15:47+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class ConsultationMapperImpl implements ConsultationMapper {

    @Override
    public ConsultationResponse toResponse(Consultation c) {
        if ( c == null ) {
            return null;
        }

        ConsultationResponse.ConsultationResponseBuilder consultationResponse = ConsultationResponse.builder();

        consultationResponse.dossierMedicalId( cDossierMedicalId( c ) );
        consultationResponse.medecinId( cMedecinId( c ) );
        consultationResponse.patientId( cPatientId( c ) );
        consultationResponse.ordonnanceId( cOrdonnanceId( c ) );
        consultationResponse.id( c.getId() );
        consultationResponse.date( c.getDate() );
        consultationResponse.motif( c.getMotif() );
        consultationResponse.diagnostic( c.getDiagnostic() );
        consultationResponse.compteRendu( c.getCompteRendu() );
        consultationResponse.typeConsultation( c.getTypeConsultation() );

        consultationResponse.medecinNomComplet( c.getMedecin().getPrenom() + " " + c.getMedecin().getNom() );
        consultationResponse.patientNomComplet( c.getPatient().getPrenom() + " " + c.getPatient().getNom() );

        return consultationResponse.build();
    }

    private UUID cDossierMedicalId(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        DossierMedical dossierMedical = consultation.getDossierMedical();
        if ( dossierMedical == null ) {
            return null;
        }
        UUID id = dossierMedical.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID cMedecinId(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        Medecin medecin = consultation.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        UUID id = medecin.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID cPatientId(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        Patient patient = consultation.getPatient();
        if ( patient == null ) {
            return null;
        }
        UUID id = patient.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID cOrdonnanceId(Consultation consultation) {
        if ( consultation == null ) {
            return null;
        }
        Ordonnance ordonnance = consultation.getOrdonnance();
        if ( ordonnance == null ) {
            return null;
        }
        UUID id = ordonnance.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
