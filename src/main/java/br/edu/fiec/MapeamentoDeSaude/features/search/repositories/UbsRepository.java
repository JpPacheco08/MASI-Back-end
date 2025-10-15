package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UbsRepository extends JpaRepository<Ubs, Long> {
    Optional<Ubs> findByNomeUbs(String nomeUbs);
}