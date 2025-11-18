package br.edu.fiec.MapeamentoDeSaude.features.search.services;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.EnderecoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDistanciaDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UbsService {

    // Métodos CRUD Padrão
    Ubs createUbs(UbsDTO ubsDto);
    Ubs getUbsByName(String name);
    Optional<Ubs> getById(UUID uuid);
    List<Ubs> getAllUbs();
    Ubs updateUbs(String name, UbsDTO ubsDto);
    void deleteUbs(UUID uuid);

    // 👇 O NOVO MÉTODO (Baseado na foto/funcionalidade de proximidade)
    List<UbsDistanciaDTO> findUbsMaisProximas(EnderecoDTO enderecoDTO);
}