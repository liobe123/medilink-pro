package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.ConsultationResponse;
import com.medilinkpro.backend.dto.response.DossierMedicalResponse;
import com.medilinkpro.backend.dto.response.ResultatAnalyseResponse;
import com.medilinkpro.backend.entity.Consultation;
import com.medilinkpro.backend.entity.DossierMedical;
import com.medilinkpro.backend.entity.Patient;
import com.medilinkpro.backend.entity.ResultatAnalyse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:51:40+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class DossierMedicalMapperImpl implements DossierMedicalMapper {

    @Autowired
    private ConsultationMapper consultationMapper;

    @Override
    public DossierMedicalResponse toResponse(DossierMedical d) {
        if ( d == null ) {
            return null;
        }

        DossierMedicalResponse.DossierMedicalResponseBuilder dossierMedicalResponse = DossierMedicalResponse.builder();

        dossierMedicalResponse.patientId( dPatientId( d ) );
        dossierMedicalResponse.id( d.getId() );
        dossierMedicalResponse.dateCreation( d.getDateCreation() );
        dossierMedicalResponse.derniereMiseAJour( d.getDerniereMiseAJour() );
        dossierMedicalResponse.statut( d.getStatut() );
        dossierMedicalResponse.chiffrementActif( d.isChiffrementActif() );
        dossierMedicalResponse.consultations( consultationListToConsultationResponseList( d.getConsultations() ) );
        dossierMedicalResponse.resultatsAnalyses( resultatAnalyseListToResultatAnalyseResponseList( d.getResultatsAnalyses() ) );

        dossierMedicalResponse.patientNomComplet( d.getPatient().getPrenom() + " " + d.getPatient().getNom() );

        return dossierMedicalResponse.build();
    }

    @Override
    public ResultatAnalyseResponse toResponse(ResultatAnalyse r) {
        if ( r == null ) {
            return null;
        }

        ResultatAnalyseResponse.ResultatAnalyseResponseBuilder resultatAnalyseResponse = ResultatAnalyseResponse.builder();

        resultatAnalyseResponse.dossierMedicalId( rDossierMedicalId( r ) );
        resultatAnalyseResponse.id( r.getId() );
        resultatAnalyseResponse.type( r.getType() );
        resultatAnalyseResponse.dateResultat( r.getDateResultat() );
        resultatAnalyseResponse.fichierUrl( r.getFichierUrl() );
        resultatAnalyseResponse.statut( r.getStatut() );
        resultatAnalyseResponse.laboratoire( r.getLaboratoire() );

        return resultatAnalyseResponse.build();
    }

    private UUID dPatientId(DossierMedical dossierMedical) {
        if ( dossierMedical == null ) {
            return null;
        }
        Patient patient = dossierMedical.getPatient();
        if ( patient == null ) {
            return null;
        }
        UUID id = patient.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<ConsultationResponse> consultationListToConsultationResponseList(List<Consultation> list) {
        if ( list == null ) {
            return null;
        }

        List<ConsultationResponse> list1 = new ArrayList<ConsultationResponse>( list.size() );
        for ( Consultation consultation : list ) {
            list1.add( consultationMapper.toResponse( consultation ) );
        }

        return list1;
    }

    protected List<ResultatAnalyseResponse> resultatAnalyseListToResultatAnalyseResponseList(List<ResultatAnalyse> list) {
        if ( list == null ) {
            return null;
        }

        List<ResultatAnalyseResponse> list1 = new ArrayList<ResultatAnalyseResponse>( list.size() );
        for ( ResultatAnalyse resultatAnalyse : list ) {
            list1.add( toResponse( resultatAnalyse ) );
        }

        return list1;
    }

    private UUID rDossierMedicalId(ResultatAnalyse resultatAnalyse) {
        if ( resultatAnalyse == null ) {
            return null;
        }
        DossierMedical dossierMedical = resultatAnalyse.getDossierMedical();
        if ( dossierMedical == null ) {
            return null;
        }
        UUID id = dossierMedical.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
