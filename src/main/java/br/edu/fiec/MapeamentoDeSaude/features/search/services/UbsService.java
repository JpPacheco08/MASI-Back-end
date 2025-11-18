package br.edu.fiec.MapeamentoDeSaude.features.search.services;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UbsService {
    Ubs createUbs(UbsDTO ubsDto);
    Ubs getUbsByName(String name);
    Optional<Ubs> getById(UUID uuid);
    List<UbsDTO> getAllUbs();
    Ubs updateUbs(String name, UbsDTO ubsDto);
    void deleteUbs(UUID uuid);
}