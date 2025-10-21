package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import lombok.Data;

@Data
public class UbsDTO {
    private String nomeUbs;
    private String endereco;
    private String telefone;
    private Double latitude;
    private Double longitude;
}