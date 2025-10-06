package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UbsAdmin extends SystemUser {
    private String cnpj;
    private String nomeDaUbs;
}