package br.edu.fiec.MapeamentoDeSaude.features.agendamento.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    private Long id;

    private String procedimento;

    private String descricao;

    private Long duracao;

    private String observacoes;
}
