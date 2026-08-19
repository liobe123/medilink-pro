package com.medilinkpro.backend.dto.mapper;

import com.medilinkpro.backend.dto.response.EtablissementResponse;
import com.medilinkpro.backend.entity.EtablissementSante;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-19T20:15:47+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.11 (Microsoft)"
)
@Component
public class EtablissementMapperImpl implements EtablissementMapper {

    @Override
    public EtablissementResponse toResponse(EtablissementSante etablissement) {
        if ( etablissement == null ) {
            return null;
        }

        EtablissementResponse.EtablissementResponseBuilder etablissementResponse = EtablissementResponse.builder();

        etablissementResponse.id( etablissement.getId() );
        etablissementResponse.nom( etablissement.getNom() );
        etablissementResponse.type( etablissement.getType() );
        etablissementResponse.adresse( etablissement.getAdresse() );
        etablissementResponse.latitude( etablissement.getLatitude() );
        etablissementResponse.longitude( etablissement.getLongitude() );
        etablissementResponse.telephone( etablissement.getTelephone() );
        List<String> list = etablissement.getSpecialitesDisponibles();
        if ( list != null ) {
            etablissementResponse.specialitesDisponibles( new ArrayList<String>( list ) );
        }

        return etablissementResponse.build();
    }
}
