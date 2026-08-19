package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.PatientResponse;
import com.medilinkpro.backend.entity.Patient;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:51:40+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public PatientResponse toResponse(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        PatientResponse.PatientResponseBuilder patientResponse = PatientResponse.builder();

        patientResponse.id( patient.getId() );
        patientResponse.nom( patient.getNom() );
        patientResponse.prenom( patient.getPrenom() );
        patientResponse.email( patient.getEmail() );
        patientResponse.telephone( patient.getTelephone() );
        patientResponse.dateNaissance( patient.getDateNaissance() );
        patientResponse.groupeSanguin( patient.getGroupeSanguin() );
        patientResponse.allergies( patient.getAllergies() );
        patientResponse.antecedents( patient.getAntecedents() );
        patientResponse.numSecuriteSociale( patient.getNumSecuriteSociale() );

        return patientResponse.build();
    }
}
