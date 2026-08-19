package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.MedecinResponse;
import com.medilinkpro.backend.entity.EtablissementSante;
import com.medilinkpro.backend.entity.Medecin;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:51:40+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class MedecinMapperImpl implements MedecinMapper {

    @Override
    public MedecinResponse toResponse(Medecin medecin) {
        if ( medecin == null ) {
            return null;
        }

        MedecinResponse.MedecinResponseBuilder medecinResponse = MedecinResponse.builder();

        medecinResponse.etablissementId( medecinEtablissementId( medecin ) );
        medecinResponse.etablissementNom( medecinEtablissementNom( medecin ) );
        medecinResponse.id( medecin.getId() );
        medecinResponse.nom( medecin.getNom() );
        medecinResponse.prenom( medecin.getPrenom() );
        medecinResponse.email( medecin.getEmail() );
        medecinResponse.telephone( medecin.getTelephone() );
        medecinResponse.specialite( medecin.getSpecialite() );
        medecinResponse.numeroOrdre( medecin.getNumeroOrdre() );
        medecinResponse.latitude( medecin.getLatitude() );
        medecinResponse.longitude( medecin.getLongitude() );
        medecinResponse.tarif( medecin.getTarif() );
        medecinResponse.verifie( medecin.isVerifie() );

        return medecinResponse.build();
    }

    private UUID medecinEtablissementId(Medecin medecin) {
        if ( medecin == null ) {
            return null;
        }
        EtablissementSante etablissement = medecin.getEtablissement();
        if ( etablissement == null ) {
            return null;
        }
        UUID id = etablissement.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String medecinEtablissementNom(Medecin medecin) {
        if ( medecin == null ) {
            return null;
        }
        EtablissementSante etablissement = medecin.getEtablissement();
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
