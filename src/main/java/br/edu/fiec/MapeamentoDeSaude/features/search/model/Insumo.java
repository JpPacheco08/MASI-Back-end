package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "insumos")
public class Insumo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "quantidade")
    private Integer quantidade;

    @Column(name = "validade")
    private LocalDate validade;
}