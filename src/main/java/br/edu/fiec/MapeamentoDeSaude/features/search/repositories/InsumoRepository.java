package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.models.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Integer> {
}