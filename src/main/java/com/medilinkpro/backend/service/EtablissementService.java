package com.medilinkpro.backend.service;

import com.medilinkpro.backend.dto.mapper.EtablissementMapper;
import com.medilinkpro.backend.dto.request.EtablissementRequest;
import com.medilinkpro.backend.dto.response.EtablissementResponse;
import com.medilinkpro.backend.entity.EtablissementSante;
import com.medilinkpro.backend.exception.ResourceNotFoundException;
import com.medilinkpro.backend.repository.EtablissementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtablissementService {

    private final EtablissementRepository etablissementRepository;
    private final EtablissementMapper etablissementMapper;

    @Transactional(readOnly = true)
    public List<EtablissementResponse> findAll() {
        return etablissementRepository.findAll().stream()
                .map(etablissementMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EtablissementResponse findById(UUID id) {
        return etablissementMapper.toResponse(getOrThrow(id));
    }

    @Transactional
    public EtablissementResponse create(EtablissementRequest request) {
        EtablissementSante etablissement = EtablissementSante.builder()
                .nom(request.getNom())
                .type(request.getType())
                .adresse(request.getAdresse())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .telephone(request.getTelephone())
                .specialitesDisponibles(request.getSpecialitesDisponibles() != null
                        ? new ArrayList<>(request.getSpecialitesDisponibles())
                        : new ArrayList<>())
                .build();

        return etablissementMapper.toResponse(etablissementRepository.save(etablissement));
    }

    @Transactional
    public EtablissementResponse update(UUID id, EtablissementRequest request) {
        EtablissementSante etablissement = getOrThrow(id);

        if (request.getNom() != null) etablissement.setNom(request.getNom());
        if (request.getType() != null) etablissement.setType(request.getType());
        if (request.getAdresse() != null) etablissement.setAdresse(request.getAdresse());
        if (request.getLatitude() != null) etablissement.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) etablissement.setLongitude(request.getLongitude());
        if (request.getTelephone() != null) etablissement.setTelephone(request.getTelephone());
        if (request.getSpecialitesDisponibles() != null) {
            etablissement.setSpecialitesDisponibles(new ArrayList<>(request.getSpecialitesDisponibles()));
        }

        return etablissementMapper.toResponse(etablissementRepository.save(etablissement));
    }

    @Transactional
    public void delete(UUID id) {
        EtablissementSante etablissement = getOrThrow(id);
        etablissementRepository.delete(etablissement);
    }

    private EtablissementSante getOrThrow(UUID id) {
        return etablissementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Etablissement non trouve avec l'id : " + id));
    }
}
