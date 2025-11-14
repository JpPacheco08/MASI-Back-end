package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import jakarta.persistence.*;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_medicamento")
    private String codigoMedicamento;

    @Column(name = "nome")
    private String nome;

    @Column(name = "principio_ativo")
    private String principio_ativo;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "quantidade_em_estoque")
    private Integer quantidadeEstoque;

    @Column(name = "lote")
    private String lote;

    @Column(name = "validade")
    private LocalDate validade;
}