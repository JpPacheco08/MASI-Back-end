package br.edu.fiec.MapeamentoDeSaude.features.agendamento.repository;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

}
