package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "insumos")
public class Insumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo;
    private String nome;
    private Integer quantidade;
    private LocalDate validade;
}