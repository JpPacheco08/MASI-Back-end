package br.edu.fiec.MapeamentoDeSaude.features.search.services;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;

import java.util.List;

public interface UbsService {
    Ubs createUbs(UbsDTO ubsDto);
    Ubs getUbsByName(String name);
    List<Ubs> getAllUbs();
    Ubs updateUbs(String name, UbsDTO ubsDto);
    void deleteUbs(String name);
}