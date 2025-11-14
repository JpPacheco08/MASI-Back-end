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
import br.edu.fiec.MapeamentoDeSaude.features.user.models.UbsAdmin; // Importe UbsAdmin
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.UserLevel;
import br.edu.fiec.MapeamentoDeSaude.features.user.repositories.UbsAdminRepository; // Importe o repositório
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AgendaServiceImpl implements AgendaService {

    // (Certifique-se que todos estes estão declarados como 'final' para o @AllArgsConstructor)
    private final UbsRepository ubsRepository;
    private final AtendimentoRepository atendimentoRepository;
    private final AgendaRepository agendaRepository;
    private final NotificationService notificationService;
    private final UbsAdminRepository ubsAdminRepository; // Este foi o repositório que adicionamos

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

        return AgendaResponseDTO.convertFromAgenda(agenda);
    }

    @Override
    public List<AgendaResponseDTO> getAllByUbs(Ubs ubs) {
        return agendaRepository.findByUbs(ubs).stream()
                .map(AgendaResponseDTO::convertFromAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendaResponseDTO> getAllByUbsAndStatus(Ubs ubs, AgendaStatus status) {
        return agendaRepository.findByUbsAndStatus(ubs, status).stream()
                .map(AgendaResponseDTO::convertFromAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendaResponseDTO> getByUser(User user) {
        return agendaRepository.findByUser(user).stream()
                .map(AgendaResponseDTO::convertFromAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public AgendaResponseDTO getById(String id) {
        return agendaRepository.findById(UUID.fromString(id))
                .map(AgendaResponseDTO::convertFromAgenda)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));
    }

    @Override
    public AgendaResponseDTO aprovaAgenda(String agendaId, User user) {
        // (Opcional) Verifica se o admin pertence à UBS da agenda

        // ---- INÍCIO DA CORREÇÃO ----
        // 1. Encontra o perfil do admin
        UbsAdmin admin = ubsAdminRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Perfil de UBS Admin não encontrado."));
        // 2. Encontra a UBS real usando o nome que está no perfil do admin
        Ubs ubs = ubsRepository.findByNomeUbs(admin.getNomeDaUbs())
                .orElseThrow(() -> new RuntimeException("UBS ligada ao admin (" + admin.getNomeDaUbs() + ") não encontrada."));
        // ---- FIM DA CORREÇÃO ----

        Agenda agenda = agendaRepository.findById(UUID.fromString(agendaId)).orElseThrow();

        if (!agenda.getUbs().getId().equals(ubs.getId()) && !user.getAccessLevel().equals(UserLevel.ADMIN)) {
            throw new AccessDeniedException("Você só pode aprovar agendamentos da sua UBS.");
        }

        agenda.setStatus(AgendaStatus.APROVADO);
        Agenda agendaSalva = agendaRepository.save(agenda); // Salva a agenda atualizada

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

        return AgendaResponseDTO.convertFromAgenda(agendaSalva);
    }


    @Override
    public void cancelarAgenda(String agendaId, User user) {
        Agenda agenda = agendaRepository.findById(UUID.fromString(agendaId))
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        // 1. Verifica se o agendamento pertence ao usuário (paciente)
        if (!agenda.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Você não tem permissão para cancelar este agendamento.");
        }

        // 2. REGRA DE 24 HORAS
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limiteParaCancelar = agenda.getHorario().minusHours(24);

        if (agora.isAfter(limiteParaCancelar)) {
            throw new RuntimeException("Agendamentos só podem ser cancelados com mais de 24 horas de antecedência.");
        }

        // 3. Se passou nas verificações, cancela
        agenda.setStatus(AgendaStatus.CANCELADO);
        agendaRepository.save(agenda);
    }


    @Override
    public List<AgendaResponseDTO> getAgendas(User user) {
        if (UserLevel.UBSADMIN.equals(user.getAccessLevel())) {

            // ---- INÍCIO DA CORREÇÃO ----
            // 1. Encontra o perfil do admin
            UbsAdmin admin = ubsAdminRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Perfil de UBS Admin não encontrado."));
            // 2. Encontra a UBS real
            Ubs ubs = ubsRepository.findByNomeUbs(admin.getNomeDaUbs())
                    .orElseThrow(() -> new RuntimeException("UBS ligada ao admin (" + admin.getNomeDaUbs() + ") não encontrada."));
            // ---- FIM DA CORREÇÃO ----

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