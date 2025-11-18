package br.edu.fiec.MapeamentoDeSaude.features.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyUserDto {
    String nome;
    String email;
    String cpf;
    String telefone;
    String cnpj;
    Double latitude;
    Double longitude;
    String crm; // Exemplo para um médico
    String nomeDaEmpresa; // Exemplo para uma UBS
    String tipo;
    String picture;
}