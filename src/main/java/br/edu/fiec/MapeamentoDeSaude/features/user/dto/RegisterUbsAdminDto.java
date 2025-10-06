package br.edu.fiec.MapeamentoDeSaude.features.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUbsAdminDto extends UserDto {
    @NotNull(message = "O campo 'cnpj' é obrigatório")
    private String cnpj;

    @NotNull(message = "O campo 'nomeDaUbs' é obrigatório")
    private String nomeDaUbs;
}