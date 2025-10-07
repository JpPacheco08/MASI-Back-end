package br.edu.fiec.MapeamentoDeSaude.features.search.services;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.VaccineDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Vaccine;

import java.util.List;

public interface VaccineService {
    Vaccine createVaccine(VaccineDTO vaccineDto);
    Vaccine getVaccineByName(String name);
    List<Vaccine> getAllVaccines();
    Vaccine updateVaccine(String name, VaccineDTO vaccineDto);
    void deleteVaccine(String name);
}