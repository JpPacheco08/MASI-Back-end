package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import br.edu.fiec.MapeamentoDeSaude.features.user.models.UbsAdmin;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tabela_ubs")
public class Ubs {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome")
    private String nomeUbs;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column
    private User user;

}
