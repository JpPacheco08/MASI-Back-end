package br.edu.fiec.MapeamentoDeSaude.features.agendamento.models;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne // <-- CORREÇÃO AQUI
    private User user;

    @ManyToOne // <-- CORREÇÃO AQUI
    private Ubs ubs;

    private AgendaStatus status;

    private TipoDePaciente tipoDePaciente;

    @ManyToOne // <-- (Se Atendimento for uma entidade, provavelmente é @ManyToOne também)
    private Atendimento atendimento;

    private LocalDateTime horario;

    private Long duracao;
}