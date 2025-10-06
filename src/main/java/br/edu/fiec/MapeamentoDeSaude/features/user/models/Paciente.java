package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Paciente extends SystemUser {
    private String cpf;
    private String cidade;
    private String cep;
}