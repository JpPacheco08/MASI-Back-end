package br.edu.fiec.MapeamentoDeSaude.features.agendamento.repository;


import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Agenda;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.AgendaStatus;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface AgendaRepository extends JpaRepository<Agenda, UUID> {
    List<Agenda> findByUser(User user);
    List<Agenda> findByHorario(Date horario);
    List<Agenda> findByStatus(AgendaStatus status);
    List<Agenda> findByUbs(Ubs ubs);
}
