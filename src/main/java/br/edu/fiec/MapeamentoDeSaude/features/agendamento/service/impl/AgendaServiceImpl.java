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
import org.springframework.security.access.AccessDeniedException; // 1. IMPORTAR
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;// 2. IMPORTAR
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // 3. IMPORTAR

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

        // ADICIONADO: Retornar o DTO convertido em vez de null
        return AgendaResponseDTO.convertFromAgenda(agenda);
    }

    @Override
    public List<AgendaResponseDTO> getAllByUbs(Ubs ubs) {
        // ADICIONADO: Lógica de busca
        return agendaRepository.findByUbs(ubs).stream()
                .map(AgendaResponseDTO::convertFromAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendaResponseDTO> getAllByUbsAndStatus(Ubs ubs, AgendaStatus status) {
        // ADICIONADO: Lógica de busca com o método que criamos no repositório
        return agendaRepository.findByUbsAndStatus(ubs, status).stream()
                .map(AgendaResponseDTO::convertFromAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public List<AgendaResponseDTO> getByUser(User user) {
        // ADICIONADO: Lógica de busca
        return agendaRepository.findByUser(user).stream()
                .map(AgendaResponseDTO::convertFromAgenda)
                .collect(Collectors.toList());
    }

    @Override
    public AgendaResponseDTO getById(String id) {
        // ADICIONADO: Lógica de busca
        return agendaRepository.findById(UUID.fromString(id))
                .map(AgendaResponseDTO::convertFromAgenda)
                .orElseThrow(() -> new RuntimeException("Agenda não encontrada"));
    }

    @Override
    public AgendaResponseDTO aprovaAgenda(String agendaId, User user) {
        // (Opcional) Verifica se o admin pertence à UBS da agenda
        Ubs ubs = ubsRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Perfil de UBS Admin não encontrado."));
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

        // ADICIONADO: Retornar o DTO convertido em vez de null
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
        LocalDateTime limiteParaCancelar = agenda.getHorario().minusHours(24); // <--- LINHA DO ERRO

        // Se "agora" estiver depois do limite, lança o erro
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
            Ubs ubs = ubsRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Perfil de UBS Admin não encontrado."));
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