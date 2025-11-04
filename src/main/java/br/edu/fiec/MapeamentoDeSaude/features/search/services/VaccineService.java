package br.edu.fiec.MapeamentoDeSaude.features.search.services;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.VaccineDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Vaccine;

import java.util.List;
import java.util.UUID;

public interface VaccineService {
    Vaccine createVaccine(VaccineDTO vaccineDto);
    Vaccine getVaccineByName(String name);
    List<Vaccine> getAllVaccines();
    Vaccine updateVaccine(String name, VaccineDTO vaccineDto);
    void deleteVaccine(UUID uuid);
}