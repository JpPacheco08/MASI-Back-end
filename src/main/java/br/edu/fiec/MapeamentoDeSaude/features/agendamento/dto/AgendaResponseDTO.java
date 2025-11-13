package br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Agenda;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.Atendimento;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.TipoDePaciente;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class AgendaResponseDTO {

    private String ubsId;
    private String ubsName;
    private LocalDateTime horario;
    private String userId;
    private String userName;
    private TipoDePaciente tipoDePaciente;
    private Atendimento atendimento;

    public static AgendaResponseDTO convertFromAgenda(Agenda agenda) {
        return AgendaResponseDTO.builder()
                .atendimento(agenda.getAtendimento())
                .ubsId(String.valueOf(agenda.getUbs().getId()))
                .userId(String.valueOf(agenda.getUser().getId()))
                .horario(agenda.getHorario())
                .tipoDePaciente(agenda.getTipoDePaciente())
                .ubsName(agenda.getUbs().getNomeUbs())
                .userName(agenda.getUser().getName())
                .build();
    }

}
