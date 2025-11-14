package br.edu.fiec.MapeamentoDeSaude.features.search.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User; // 1. IMPORTAR O USER
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UbsRepository extends JpaRepository<Ubs, UUID> {
    Optional<Ubs> findByNomeUbs(String nomeUbs);

    // 2. 👇 ADICIONE ESTA LINHA FALTANTE
    Optional<Ubs> findByUser(User user);
}