package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class VaccineDTO {
    private String codigoVacina;
    private String nomeVacina;
    private String descVacina;
    private String tratamento;
    private String faixaEtaria;
    private String intervaloEntreDoses;
    private Integer numeroDoses;
    private Integer quantidadeEstoque;
    private String lote;
    private Date dataFabricacao;
    private Date validade;
}