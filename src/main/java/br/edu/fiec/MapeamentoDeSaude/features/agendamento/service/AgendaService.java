package br.edu.fiec.MapeamentoDeSaude.features.agendamento.service;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto.AgendaRequestDTO;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto.AgendaResponseDTO;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Agenda;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.AgendaStatus;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;

import java.util.List;

public interface AgendaService {
    AgendaResponseDTO createAgenda(AgendaRequestDTO agendaRequestDTO, User user);
    List<AgendaResponseDTO> getAllByUbs(Ubs ubs);
    List<AgendaResponseDTO> getAllByUbsAndStatus(Ubs ubs, AgendaStatus status);
    List<AgendaResponseDTO> getByUser(User user);
    AgendaResponseDTO getById(String id);

    Object aprovaAgenda(String agendaId, User user);

    List<AgendaResponseDTO> getAgendas(User user);
}
