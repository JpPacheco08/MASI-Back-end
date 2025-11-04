package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InsumoDTO {

    private String tipo;
    private String nome;
    private Integer quantidade;
    private LocalDate validade;

}
