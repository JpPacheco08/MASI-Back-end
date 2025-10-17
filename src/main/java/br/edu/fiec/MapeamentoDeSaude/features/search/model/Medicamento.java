package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String principio_ativo;
    private String tipo;
    private Integer quantidade;
    private LocalDate validade;
}