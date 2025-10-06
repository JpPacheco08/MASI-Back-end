package br.edu.fiec.MapeamentoDeSaude.features.user.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.user.models.Paciente;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Optional<Paciente> findByUser(User user);
}