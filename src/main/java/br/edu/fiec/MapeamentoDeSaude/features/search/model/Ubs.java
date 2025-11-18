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
    private Long id;

    @Column(name = "nome")
    private String nomeUbs;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "cep")
    private String cep;

    @Column
    private User user;

}
