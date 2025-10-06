package br.edu.fiec.MapeamentoDeSaude.features.user.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.user.models.UbsAdmin;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UbsAdminRepository extends JpaRepository<UbsAdmin, UUID> {
    Optional<UbsAdmin> findByUser(User user);
}