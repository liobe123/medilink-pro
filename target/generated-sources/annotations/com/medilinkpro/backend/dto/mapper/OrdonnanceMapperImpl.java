package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.OrdonnanceResponse;
import com.medilinkpro.backend.entity.Consultation;
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
public class OrdonnanceMapperImpl implements OrdonnanceMapper {

    @Override
    public OrdonnanceResponse toResponse(Ordonnance o) {
        if ( o == null ) {
            return null;
        }

        OrdonnanceResponse.OrdonnanceResponseBuilder ordonnanceResponse = OrdonnanceResponse.builder();

        ordonnanceResponse.consultationId( oConsultationId( o ) );
        ordonnanceResponse.medecinId( oMedecinId( o ) );
        ordonnanceResponse.patientId( oPatientId( o ) );
        ordonnanceResponse.id( o.getId() );
        ordonnanceResponse.dateEmission( o.getDateEmission() );
        ordonnanceResponse.medicaments( o.getMedicaments() );
        ordonnanceResponse.posologie( o.getPosologie() );
        ordonnanceResponse.signatureElectronique( o.getSignatureElectronique() );
        ordonnanceResponse.codeQr( o.getCodeQr() );

        ordonnanceResponse.medecinNomComplet( o.getMedecin().getPrenom() + " " + o.getMedecin().getNom() );
        ordonnanceResponse.patientNomComplet( o.getPatient().getPrenom() + " " + o.getPatient().getNom() );

        return ordonnanceResponse.build();
    }

    private UUID oConsultationId(Ordonnance ordonnance) {
        if ( ordonnance == null ) {
            return null;
        }
        Consultation consultation = ordonnance.getConsultation();
        if ( consultation == null ) {
            return null;
        }
        UUID id = consultation.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID oMedecinId(Ordonnance ordonnance) {
        if ( ordonnance == null ) {
            return null;
        }
        Medecin medecin = ordonnance.getMedecin();
        if ( medecin == null ) {
            return null;
        }
        UUID id = medecin.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID oPatientId(Ordonnance ordonnance) {
        if ( ordonnance == null ) {
            return null;
        }
        Patient patient = ordonnance.getPatient();
        if ( patient == null ) {
            return null;
        }
        UUID id = patient.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
