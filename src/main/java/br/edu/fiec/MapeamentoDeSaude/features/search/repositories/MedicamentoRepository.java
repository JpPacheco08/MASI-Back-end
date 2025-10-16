package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.models.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {
}