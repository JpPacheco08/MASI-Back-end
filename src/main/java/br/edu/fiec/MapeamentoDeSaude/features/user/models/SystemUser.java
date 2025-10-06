package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass // <-- Anotação MUITO importante!
public abstract class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    User user;
}