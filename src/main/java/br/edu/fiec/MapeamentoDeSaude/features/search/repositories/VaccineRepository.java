package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {
    Optional<Vaccine> findByNomeVacina(String nomeVacina);
}