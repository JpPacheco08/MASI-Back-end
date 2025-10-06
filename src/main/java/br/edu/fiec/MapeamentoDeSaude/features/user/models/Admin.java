package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends SystemUser {
    private String cargo; // Ex: "Desenvolvedor", "Suporte"
}