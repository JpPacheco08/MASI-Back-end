package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UbsRepository extends JpaRepository<Ubs, UUID> {
    Optional<Ubs> findByNomeUbs(String nomeUbs);
    Optional<Ubs> findByUser(User user);
}