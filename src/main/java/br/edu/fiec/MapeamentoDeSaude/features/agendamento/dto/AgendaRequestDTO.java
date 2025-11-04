package br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.models.TipoDePaciente;
import lombok.Data;

import java.util.Date;

@Data
public class AgendaRequestDTO {

    private TipoDePaciente tipoDePaciente;

    private String ubsId;

    private String atendimentoId;

    private Date horario;
}
