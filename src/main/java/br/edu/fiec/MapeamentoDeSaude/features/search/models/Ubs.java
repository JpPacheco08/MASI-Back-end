// A linha do pacote foi alterada de '...search.entity' para '...search.model'
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
@Table(name = "tabela_ubs")
public class Ubs {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_ubs")
    private String nomeUbs;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "telefone")
    private String telefone;
}