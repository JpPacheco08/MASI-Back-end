package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicamentoDTO {
    private String nome;
    private String principio_ativo;
    private String tipo;
    private Integer quantidade;
    private LocalDate validade;
}
