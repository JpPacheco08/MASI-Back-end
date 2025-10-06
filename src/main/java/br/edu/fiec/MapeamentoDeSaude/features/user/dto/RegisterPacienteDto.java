package br.edu.fiec.MapeamentoDeSaude.features.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPacienteDto extends UserDto {
    @NotNull(message = "O campo 'cpf' é obrigatório")
    private String cpf;
    private String cidade;
    private String cep;
}