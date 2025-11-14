package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, UUID> {
    Optional<Medicamento> getMedicamentoByNome(String nomeMedicamento);

}