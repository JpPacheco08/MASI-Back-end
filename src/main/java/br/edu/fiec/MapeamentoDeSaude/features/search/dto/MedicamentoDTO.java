package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicamentoDTO {
    private String codigoMedicamento;
    private String nome;
    private String principio_ativo;
    private String tipo;
    private Integer quantidadeEstoque;
    private String lote;
    private LocalDate validade;
}
