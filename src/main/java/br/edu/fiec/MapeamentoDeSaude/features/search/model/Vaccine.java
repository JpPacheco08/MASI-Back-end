package br.edu.fiec.MapeamentoDeSaude.features.search.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tabela_vacinas")
public class Vaccine {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_vacina")
    private String nomeVacina;
    @Column(name = "desc_vacina")
    private String descVacina;
}