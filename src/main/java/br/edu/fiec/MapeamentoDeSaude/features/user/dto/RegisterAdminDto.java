package br.edu.fiec.MapeamentoDeSaude.features.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAdminDto extends UserDto {
    @NotNull(message = "O campo 'cargo' é obrigatório")
    private String cargo;
}