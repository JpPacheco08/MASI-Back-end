package br.edu.fiec.MapeamentoDeSaude.features.agendamento.repositories;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
}