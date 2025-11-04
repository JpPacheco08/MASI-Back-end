package br.edu.fiec.MapeamentoDeSaude.features.search.services;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.MedicamentoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Medicamento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MedicamentoService {
    Medicamento createMedicamento(MedicamentoDTO medicamentoDTO);
    Medicamento getByNome(String name);
    Optional<Medicamento> getById(UUID uuid);
    List<Medicamento> getAll();
    Medicamento updateMedicamento(String name, MedicamentoDTO medicamentoDTO);
    void deleteMedicamento(UUID uuid);
}
