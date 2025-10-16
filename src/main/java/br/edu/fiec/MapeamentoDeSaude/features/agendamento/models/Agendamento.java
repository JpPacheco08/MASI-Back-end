package br.edu.fiec.MapeamentoDeSaude.features.agendamento.models;

import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "id_ubs")
    private Ubs ubs;

    @Column(name = "tipo")
    private String tipo;
}