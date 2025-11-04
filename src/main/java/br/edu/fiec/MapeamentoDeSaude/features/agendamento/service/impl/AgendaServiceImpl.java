package br.edu.fiec.MapeamentoDeSaude.features.agendamento.service.impl;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto.AgendaRequestDTO;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto.AgendaResponseDTO;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Agenda;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.AgendaStatus;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Atendimento;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.repository.AgendaRepository;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.repository.AtendimentoRepository;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.service.AgendaService;
import br.edu.fiec.MapeamentoDeSaude.features.firebase.models.dto.NotificationMessage;
import br.edu.fiec.MapeamentoDeSaude.features.firebase.services.NotificationService;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.UserLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AgendaServiceImpl implements AgendaService {

    private UbsRepository ubsRepository;
    private AtendimentoRepository atendimentoRepository;
    private AgendaRepository agendaRepository;
    private NotificationService notificationService;

    @Override
    public AgendaResponseDTO createAgenda(AgendaRequestDTO agendaRequestDTO, User user) {
        Ubs ubs = ubsRepository.findById(UUID.fromString(agendaRequestDTO.getUbsId())).orElseThrow();
        Atendimento atendimento = atendimentoRepository.findById(Long.valueOf(agendaRequestDTO.getAtendimentoId())).orElseThrow();
        Agenda agenda = agendaRepository.save(Agenda.builder()
                        .atendimento(atendimento)
                        .ubs(ubs)
                        .user(user)
                        .horario(agendaRequestDTO.getHorario())
                        .duracao(atendimento.getDuracao())
                        .status(AgendaStatus.PENDENTE)
                .build());
        return null;
    }

    @Override
    public List<AgendaResponseDTO> getAllByUbs(Ubs ubs) {
        return List.of();
    }

    @Override
    public List<AgendaResponseDTO> getAllByUbsAndStatus(Ubs ubs, AgendaStatus status) {
        return List.of();
    }

    @Override
    public List<AgendaResponseDTO> getByUser(User user) {
        return List.of();
    }

    @Override
    public AgendaResponseDTO getById(String id) {
        return null;
    }

    @Override

    public Object aprovaAgenda(String agendaId, User user) {
        Ubs ubs = ubsRepository.findByUser(user).orElseThrow();
        Agenda agenda = agendaRepository.findById(UUID.fromString(agendaId)).orElseThrow();
        agenda.setStatus(AgendaStatus.APROVADO);
        agendaRepository.save(agenda);
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .message(String.format("Agendado para %s %s", agenda.getHorario(), agenda.getUser().getName()))
                .authUserId(String.valueOf(agenda.getUser().getId()))
                .title("Agendamento Aprovado")
                .build();
         try {
             notificationService.sendNotificationToUser(notificationMessage);
         } catch (Exception exception){
             log.error("Ocorreu um erro" , exception);
         }
        return null;
    }

    @Override
    public List<AgendaResponseDTO> getAgendas(User user) {
        if (UserLevel.UBSADMIN.equals(user.getAccessLevel())) {
            Ubs ubs = ubsRepository.findByUser(user).orElseThrow();
            List<Agenda> agendas = agendaRepository.findByUbs(ubs);
            return agendas.stream().map(AgendaResponseDTO::convertFromAgenda).toList();
        } else if (UserLevel.USER.equals(user.getAccessLevel())) {
            List<Agenda> agendas = agendaRepository.findByUser(user);
            return agendas.stream().map(AgendaResponseDTO::convertFromAgenda).toList();
        } else {
            List<Agenda> agendas = agendaRepository.findAll();
            return agendas.stream().map(AgendaResponseDTO::convertFromAgenda).toList();
        }
    }
}
