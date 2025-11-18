package br.edu.fiec.MapeamentoDeSaude.features.search.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnderecoDTO {
    @NotBlank(message = "O endereço não pode ser vazio")
    private String endereco;
}